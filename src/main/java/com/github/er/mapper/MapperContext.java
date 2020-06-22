package com.github.er.mapper;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ER
 * <p>
 * package private and must stay this way
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
final class MapperContext<IN, T, R, OUT> {

    private final boolean directly;

    private final Function<IN, T> getter;
    private final BiConsumer<OUT, R> setter;

    private final Predicate<T> inPredicate;
    private final Predicate<R> outPredicate;
    private final Function<T, R> mapper;

}
