package com.github.er.mapper;

import com.github.er.mapper.exception.MethodNotFoundException;
import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Getter;

/**
 * @author ER
 */
public interface Registrar<IN, OUT> {

    /**
     * <p>fields have the same name</p>
     * <p>fields have the same non-primitive types</p>
     *
     * @throws MethodNotFoundException
     *     thrown when a method resolution fails
     */
    Registrar<IN, OUT> withMapping(String fieldName, Type fieldType)
        throws MethodNotFoundException;

    /**
     * <p>fields have different names</p>
     * <p>fields have the same type</p>
     *
     * @throws MethodNotFoundException
     *     thrown when a method resolution fails
     */
    Registrar<IN, OUT> withMapping(String inputFieldName, String outputFieldName, Type fieldType)
        throws MethodNotFoundException;

    /**
     * you could use this one for mapping everything, basically.
     * I am leaving in here for various purposes : like mapping multiple fields to one.
     * If you can use one of the more fine grained methods, please do.
     */
    <R> Registrar<IN, OUT> directly(String outputFieldName, Type outputFieldType, Function<IN, R> mapper)
        throws MethodNotFoundException;

    /**
     * if 'withMapping' are not enough, the most probably you require this method. It allows to set predicates,
     * custom types or mappers between fields.
     *
     * @throws MethodNotFoundException
     *     thrown when a method resolution fails
     */
    <INNER_TYPE, OUTER_TYPE> Registrar<IN, OUT> withBuilder(Builder<INNER_TYPE, OUTER_TYPE> builder)
        throws MethodNotFoundException;

    @Getter
    class Builder<INNER_TYPE, OUTER_TYPE> {

        private String inputFieldName;
        private String outputFieldName;
        private Type inputFieldType;
        private Type outputFieldType;

        private Predicate<? super INNER_TYPE> inputPredicate = x -> true;
        private Predicate<? super OUTER_TYPE> outputPredicate = x -> true;
        private Function<? super INNER_TYPE, ? extends OUTER_TYPE> mapper;

        /**
         * both "IN" and "OUT" fields have the same name
         */
        public Builder<INNER_TYPE, OUTER_TYPE> sameFieldNames(String fieldName) {
            inputFieldName = fieldName;
            outputFieldName = fieldName;
            return this;
        }

        /**
         * both "IN" and "OUT" fields have the same type
         */
        @SuppressWarnings("unchecked")
        public Builder<INNER_TYPE, OUTER_TYPE> sameFieldTypes(Type type) {
            inputFieldType = type;
            outputFieldType = type;
            mapper = (Function<INNER_TYPE, OUTER_TYPE>) Function.identity();
            return this;
        }

        /**
         * what is the name of "IN" field that is getting mapped
         */
        public Builder<INNER_TYPE, OUTER_TYPE> inputFieldName(String inputFieldName) {
            this.inputFieldName = inputFieldName;
            return this;
        }

        public Builder<INNER_TYPE, OUTER_TYPE> outputFieldName(String outputFieldName) {
            this.outputFieldName = outputFieldName;
            return this;
        }

        public Builder<INNER_TYPE, OUTER_TYPE> inputFieldType(Type inType) {
            inputFieldType = inType;
            return this;
        }

        public Builder<INNER_TYPE, OUTER_TYPE> outputFieldType(Type outType) {
            outputFieldType = outType;
            return this;
        }

        public Builder<INNER_TYPE, OUTER_TYPE> mapper(Function<? super INNER_TYPE, ? extends OUTER_TYPE> mapper) {
            this.mapper = mapper;
            return this;
        }

        /**
         * should we map or not based on this Predicate
         */
        public Builder<INNER_TYPE, OUTER_TYPE> inputPredicate(Predicate<? super INNER_TYPE> inputPredicate) {
            this.inputPredicate = inputPredicate;
            return this;
        }

        /**
         * should be map or not based on this Predicate (after applying the Mapper)
         */
        public Builder<INNER_TYPE, OUTER_TYPE> outputPredicate(Predicate<? super OUTER_TYPE> outputPredicate) {
            this.outputPredicate = outputPredicate;
            return this;
        }

    }

}
