package com.github.er.mapper;

import com.github.er.mapper.exception.MethodNotFoundException;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author ER
 * <p>
 * various package private methods
 */
final class Utils {

    public static final Lookup LOOKUP = MethodHandles.lookup();

    // ---------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------

    /**
     * reference to a constructor of type OUT.
     * this assumes a default one is present
     */
    static <OUT> Supplier<OUT> constructor(Class<OUT> out) {

        try {

            MethodHandle supplier = LOOKUP.findConstructor(out, MethodType.methodType(void.class));

            CallSite callSite = LambdaMetafactory.metafactory(
                LOOKUP,
                "get",
                MethodType.methodType(Supplier.class),
                supplier.type().erase(),
                supplier,
                MethodType.methodType(out));

            return (Supplier<OUT>) callSite.getTarget().invokeExact();

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------

    /**
     * from some IN to one of it's fields. For example: IN == Person, IN_PARAM_TYPE == String (firstName)
     */
    static <IN, IN_PARAM_TYPE> Function<IN, IN_PARAM_TYPE> getter(Class<?> in, String inField, Type inFieldType) {

        String methodName = Utils.getterCamelCase(inField);
        Class<?> rawClass = Utils.rawType(inFieldType, false);

        try {

            MethodHandle getter;

            try {
                getter = LOOKUP.findVirtual(
                    in, methodName, MethodType.methodType(rawClass)
                );
            } catch (NoSuchMethodException no) {

                // let's check if this was a generic method
                Type genericReturnType = in.getMethod(methodName).getGenericReturnType();
                if (genericReturnType.getClass() != Class.class) {

                    Class<?> erasedClass = Utils.rawType(genericReturnType, false);
                    if (erasedClass.isAssignableFrom(rawClass)) {
                        getter = LOOKUP.findVirtual(
                            in, methodName, MethodType.methodType(erasedClass)
                        );
                    } else {
                        throw no;
                    }

                } else {
                    throw no;
                }
            }

            CallSite callSite = LambdaMetafactory.metafactory(
                LOOKUP,
                "apply",
                MethodType.methodType(Function.class),
                getter.type().wrap().erase(),
                getter,
                MethodType.methodType(rawClass, in));

            return (Function<IN, IN_PARAM_TYPE>) callSite.getTarget().invokeExact();

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    // ---------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------

    /**
     * setter as a BiConsumer
     */
    static <OUT, OUT_PARAM_TYPE> BiConsumer<OUT, OUT_PARAM_TYPE> setter(Class<?> out, String outField, Type outFieldType) {

        String methodName = Utils.setterCamelCase(outField);
        Class<?> rawClass = Utils.rawType(outFieldType, false);

        try {

            MethodHandle setter;

            try {
                setter = LOOKUP.findVirtual(
                    out, methodName, MethodType.methodType(void.class, rawClass)
                );
            } catch (NoSuchMethodException no) {

                // this is not completely safe...
                Method met = Arrays.stream(out.getDeclaredMethods())
                                   .filter(m -> m.getName().equals(methodName))
                                   .filter(m -> m.getParameterCount() == 1)
                                   .findFirst()
                                   .orElseThrow(() -> new MethodNotFoundException(out, rawClass, methodName));
                Type genericReturnType = met.getGenericParameterTypes()[0];

                // let's see if this returned a generic type
                if (genericReturnType.getClass() != Class.class) {

                    Class<?> erasedClass = Utils.rawType(genericReturnType, false);
                    if (erasedClass.isAssignableFrom(rawClass)) {
                        setter = LOOKUP.findVirtual(
                            out, methodName, MethodType.methodType(void.class, erasedClass)
                        );
                    } else {
                        throw no;
                    }
                } else {
                    throw no;
                }

            }

            CallSite callSite = LambdaMetafactory.metafactory(
                LOOKUP,
                "accept",
                MethodType.methodType(BiConsumer.class),
                setter.type().wrap().erase().changeReturnType(void.class),
                setter,
                MethodType.methodType(void.class, out, rawClass).wrap().changeReturnType(void.class));

            return (BiConsumer<OUT, OUT_PARAM_TYPE>) callSite.getTarget().invokeExact();

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

    }

    // ---------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------

    static void precondition(String s) {
        if (s.trim().length() == 0) {
            throw new AssertionError("field name must be present");
        }
    }

    static String setterCamelCase(String input) {
        char first = Character.toUpperCase(input.charAt(0));
        return "set" + first + input.substring(1);
    }

    static String getterCamelCase(String input) {
        char first = Character.toUpperCase(input.charAt(0));
        return "get" + first + input.substring(1);
    }

    // wildcard capture
    static <T, R> R map(Function<T, R> mapper, Object against) {
        return mapper.apply((T) against);
    }

    // wildcard capture
    static <T> boolean test(Predicate<T> p, Object against) {
        T t = (T) against;
        return p.test(t);
    }

    static Class<?> rawType(Type type, boolean root) {

        // 1. a plain Class, might be an array also.
        if (type instanceof Class) {
            Class<?> cls = (Class<?>) type;
            boolean isArray = cls.isArray();
            if (isArray && root) {
                throw new UnsupportedOperationException("IN/OUT can't be arrays, these are your pojo types");
            } else {
                return cls;
            }
        }

        // 2. TypeVariable
        if (type instanceof TypeVariable) {
            if (root) {
                // I might want to re-think and handle these, will see
                throw new UnsupportedOperationException("IN/OUT can't be generic types");
            } else {
                Type[] bounds = ((TypeVariable<?>) type).getBounds();
                // left-most bound
                return (Class<?>) bounds[0];
            }
        }

        // 3. a ParametrizedType, like Box<Integer> or List<T>. this type provides "getRawType" that I care about.
        // Ideally if callers provide a List<T> and this is IN/OUT, I should fail nicely; but I can't
        // distinguish between Box<Integer> and List<T>.
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                return (Class<?>) rawType;
            } else {
                // that is an interesting thing right here
                throw new RuntimeException("type : " + type.getTypeName() + " is not supported");
            }
        }

        // 4. generic array
        if (type instanceof GenericArrayType) {
            if (root) {
                throw new UnsupportedOperationException("IN/OUT can't be generic array types");
            }
            Type arrayType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(rawType(arrayType, false), 0).getClass();
        }

        // 5. wildcard type
        if (type instanceof WildcardType) {
            if (root) {
                throw new UnsupportedOperationException("IN/OUT can't be generic wildcard types");
            }
            return rawType(((WildcardType) type).getUpperBounds()[0], false);
        }
        throw new UnsupportedOperationException("no");

    }

}
