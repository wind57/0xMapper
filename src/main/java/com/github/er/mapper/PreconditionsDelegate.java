package com.github.er.mapper;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author ER
 * <p>
 * Just a way to delegate all the validations checks, so that {@link MappingRegistrar} stays cleaner
 */
final class PreconditionsDelegate<IN, OUT> implements Registrar<IN, OUT> {

    private static final String BUILDER_ERROR_MESSAGE = "there are properties you need to set when using a Builder: " +
        "inField, outField, inType, outType, mapper. If you do not require all these, you can use 'withMapping' overloads.";

    @Override
    public Registrar<IN, OUT> withMapping(String fieldName, Type fieldType) {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(fieldType);
        Utils.precondition(fieldName);
        return this;
    }

    @Override
    public Registrar<IN, OUT> withMapping(String inputFieldName, String outputFieldName, Type fieldType) {
        Objects.requireNonNull(inputFieldName);
        Objects.requireNonNull(outputFieldName);
        Objects.requireNonNull(fieldType);
        Utils.precondition(inputFieldName);
        Utils.precondition(outputFieldName);
        return this;
    }

    @Override
    public <INNER_TYPE, OUTER_TYPE> Registrar<IN, OUT> withBuilder(Builder<INNER_TYPE, OUTER_TYPE> builder) {
        Objects.requireNonNull(builder);
        Objects.requireNonNull(builder.getInputFieldName(), BUILDER_ERROR_MESSAGE);
        Objects.requireNonNull(builder.getOutputFieldName(), BUILDER_ERROR_MESSAGE);
        Objects.requireNonNull(builder.getInputFieldType(), BUILDER_ERROR_MESSAGE);
        Objects.requireNonNull(builder.getOutputFieldType(), BUILDER_ERROR_MESSAGE);
        Objects.requireNonNull(builder.getInputPredicate());
        Objects.requireNonNull(builder.getOutputPredicate());

        Utils.precondition(builder.getInputFieldName());
        Utils.precondition(builder.getOutputFieldName());

        Objects.requireNonNull(builder.getMapper(), BUILDER_ERROR_MESSAGE);

        return this;
    }

    @Override
    public <R> Registrar<IN, OUT> directly(String outputFieldName, Type outputFieldType, Function<IN, R> mapper) {
        Objects.requireNonNull(outputFieldName);
        Objects.requireNonNull(outputFieldType);
        Objects.requireNonNull(mapper);
        Utils.precondition(outputFieldName);
        return this;
    }

}
