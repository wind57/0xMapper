package com.github.er.mapper;

import com.github.er.mapper.exception.MethodNotFoundException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MappingRegistrar<IN, OUT> implements Registrar<IN, OUT>, Transformer<IN, OUT> {

    private final PreconditionsDelegate<IN, OUT> PRECONDITIONS = new PreconditionsDelegate<>();

    private final Class<?> rawInClass;
    private final Class<?> rawOutClass;
    private final List<MapperContext<IN, ?, ?, OUT>> mappings = new ArrayList<>();
    private final Supplier<OUT> constructor;

    @SuppressWarnings("unchecked")
    protected MappingRegistrar() {
        Class<?> cls = getClass();
        Type[] both = ((ParameterizedType) cls.getGenericSuperclass()).getActualTypeArguments();
        rawInClass = Utils.rawType(both[0], true);
        rawOutClass = Utils.rawType(both[1], true);
        constructor = (Supplier<OUT>) Utils.constructor(rawOutClass);
    }

    @Override
    public MappingRegistrar<IN, OUT> withMapping(String fieldName, Type fieldType) {
        PRECONDITIONS.withMapping(fieldName, fieldType);
        withMapping(false, fieldName, fieldName, fieldType, fieldType, x -> true, x -> true, Function.identity());
        return this;
    }

    @Override
    public MappingRegistrar<IN, OUT> withMapping(String inputFieldName, String outputFieldName, Type fieldType) {
        PRECONDITIONS.withMapping(inputFieldName, outputFieldName, fieldType);
        withMapping(false, inputFieldName, outputFieldName, fieldType, fieldType, x -> true, x -> true, Function.identity());
        return this;
    }

    @Override
    public <INNER_TYPE, OUTER_TYPE> Registrar<IN, OUT> withBuilder(Builder<INNER_TYPE, OUTER_TYPE> builder) {
        PRECONDITIONS.withBuilder(builder);
        withMapping(false, builder.getInputFieldName(), builder.getOutputFieldName(), builder.getInputFieldType(),
                    builder.getOutputFieldType(),
                    builder.getInputPredicate(), builder.getOutputPredicate(), builder.getMapper());
        return this;
    }

    @Override
    public <R> MappingRegistrar<IN, OUT> directly(String outputFieldName, Type outputFieldType, Function<IN, R> mapper) {
        PRECONDITIONS.directly(outputFieldName, outputFieldType, mapper);
        withMapping(true, null, outputFieldName, outputFieldType, outputFieldType, x -> true, x -> true, mapper);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OUT transform(IN input) {

        try {
            OUT out = constructor.get();
            for (MapperContext<IN, ?, ?, OUT> c : mappings) {

                // I can't figure out another way to do this for now
                MapperContext<IN, Object, Object, OUT> context = (MapperContext<IN, Object, Object, OUT>) c;

                //a special case when we want to map the entire IN to some out field
                if (context.isDirectly()) {
                    Object outObj = Utils.map(context.getMapper(), input);
                    context.getSetter().accept(out, outObj);
                } else {
                    Object outObj;
                    Object inObj = context.getGetter().apply(input);
                    Predicate<?> pIn = context.getInPredicate();
                    boolean mapInField = Utils.test(pIn, inObj);
                    if (mapInField) {
                        outObj = Utils.map(context.getMapper(), inObj);
                        Predicate<?> pOut = context.getOutPredicate();
                        boolean mapOutField = Utils.test(pOut, outObj);
                        if (mapOutField) {
                            context.getSetter().accept(out, outObj);
                        }
                    }
                }
            }

            return out;

        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    // Function<?, ?> instead of Function<T, R> because wildcard capture
    // would not work for two parameters
    @SuppressWarnings("unchecked")
    private <T, R> void withMapping(boolean directly,
                                    String inField,
                                    String outField,
                                    Type inFieldType,
                                    Type outFieldType,
                                    Predicate<T> inPredicate,
                                    Predicate<R> outPredicate,
                                    Function<?, ?> mapper) {

        try {

            // when mapping directly, we do not need a getter
            Function<IN, T> getter = directly ? null : Utils.getter(rawInClass, inField, inFieldType);
            BiConsumer<OUT, R> setter = Utils.setter(rawOutClass, outField, outFieldType);

            mappings.add(
                new MapperContext<>(directly, getter, setter, inPredicate, outPredicate, (Function<T, R>) mapper)
            );
        } catch (MethodNotFoundException notFound) {
            throw notFound;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }
}
