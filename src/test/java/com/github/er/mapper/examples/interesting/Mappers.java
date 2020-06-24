package com.github.er.mapper.examples.interesting;

import com.github.er.mapper.MappingRegistrar;
import com.github.er.mapper.Registrar.Builder;
import com.github.roookeee.datus.api.Datus;
import com.github.roookeee.datus.api.Mapper;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public class Mappers {

    private static final Predicate<String> PREDICATE_IN = Pattern.compile("^\\d+$").asPredicate();
    private static final Predicate<Integer> PREDICATE_OUT = x -> x >= 4;

    private static final MappingRegistrar<In<Long>, Out<Integer>> MAPPER;
    private static final Mapper<In<Long>, Out<Integer>> DATUS_MAPPER;

    static {
        MAPPER = new MappingRegistrar<>() {};

        // 1. age mapping : from "userAge" to "age" (String to int, according to the "mapper")
        // only map if Predicate (digits only) matches the given input
        MAPPER.withBuilder(
            new Builder<String, Integer>().inputFieldName("userAge")
                                          .outputFieldName("age")
                                          .inputFieldType(String.class)
                                          .outputFieldType(int.class)
                                          .inputPredicate(PREDICATE_IN)
                                          .mapper(Integer::valueOf)
        );

        // 2. color mapping: color to color (String to int, according to the "mapper")
        // only map if output matches the output Predicate (at least 4 digits)

        MAPPER.withBuilder(
            new Builder<String, Integer>().sameFieldNames("color")
                                          .inputFieldType(String.class)
                                          .outputFieldType(int.class)
                                          .mapper(String::length)
                                          .outputPredicate(PREDICATE_OUT)
        );

        // 3. map a generic List
        // inputFieldType and outputFieldType both accept a Type
        MAPPER.withBuilder(
            new Builder<List<Long>, List<Integer>>().sameFieldNames("list")
                                                    .inputFieldType(new TypeToken<List<Integer>>() {}.getType())
                                                    .outputFieldType(List.class)
                                                    .mapper(longList -> longList.stream()
                                                                                .map(Long::intValue)
                                                                                .collect(Collectors.toList()))
        );
    }

    static {
        // there is not a nice way to map conditionally in Datus:
        // https://github.com/roookeee/datus/issues/36
        // because of that I have a bit of weird hacks here
        DATUS_MAPPER = Datus.<In<Long>, Out<Integer>>forTypes()
            .mutable(Out::new)
            .from(In::getUserAge).given(PREDICATE_IN, Function.identity()).orElse("0")
            .map(Integer::parseInt).into(Out::setAge)
            .from(In::getColor).map(x -> x.length() >= 4 ? x.length() : 0).into(Out::setColor)
            .from(In::getList).map(list -> list.stream().map(Number::intValue).collect(Collectors.toList()))
            .into(Out::setList)
            .build();
    }

    @org.mapstruct.Mapper
    public interface InterestingMapper {

        InterestingMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(InterestingMapper.class);

        @BeforeMapping
        default void beforeMapping(@MappingTarget Out<Integer> target, In<Long> source) {
            if (PREDICATE_IN.test(source.getUserAge())) {
                target.setAge(Integer.parseInt(source.getUserAge()));
            }

            // map it, then check it in the @AfterMapping
            target.setColor(source.getColor().length());

            target.setList(source.getList().stream().map(Long::intValue).collect(Collectors.toList()));
        }

        @AfterMapping
        default void afterMapping(@MappingTarget Out<Integer> target, In<Long> source) {
            // I can't find a better way to say: the result should only be mapped if it matches the Predicate.
            // though this is not entirely correct. Suppose the OUT has a default value of 11, how will I
            // skip mapping if Predicate does not match, but at the same time keep 11 in place?
            if (!PREDICATE_OUT.test(target.getColor())) {
                target.setColor(0);
            }
        }

        // all the fields require some custom mapping, so I ignore them all
        @Mapping(target = "age", ignore = true)
        @Mapping(target = "color", ignore = true)
        @Mapping(target = "list", ignore = true)
        Out<Integer> inToOut(In<Long> in);

    }

    private static Out<Integer> direct(In<Long> input) {

        Out<Integer> out = new Out<>();
        if (PREDICATE_IN.test(input.getUserAge())) {
            out.setAge(Integer.parseInt(input.getUserAge()));
        }

        int color = input.getColor().length();
        if (PREDICATE_OUT.test(color)) {
            out.setColor(color);
        }

        List<Integer> list = input.getList().stream().map(Long::intValue).collect(Collectors.toList());
        out.setList(list);

        return out;
    }

    public static Function<In<Long>, Out<Integer>> mapper() {
        return MAPPER::transform;
    }

    public static Function<In<Long>, Out<Integer>> datusMapper() {
        return DATUS_MAPPER::convert;
    }

    public static Function<In<Long>, Out<Integer>> directMapper() {
        return Mappers::direct;
    }

    public static Function<In<Long>, Out<Integer>> mapStructMapper() {
        return InterestingMapper.INSTANCE::inToOut;
    }

}
