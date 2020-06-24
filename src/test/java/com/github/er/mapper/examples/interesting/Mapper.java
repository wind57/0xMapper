package com.github.er.mapper.examples.interesting;

import com.github.er.mapper.MappingRegistrar;
import com.github.er.mapper.Registrar.Builder;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Mapper {

    public static void main(String[] args) {

        In<Long> in = new In<Long>()
            .setList(List.of(1L))
            .setUserAge("57")
            .setColor("blue");

        MappingRegistrar<In<Long>, Out<Integer>> registrar = new MappingRegistrar<>() {};

        // 1. age mapping : from "userAge" to "age" (String to int, according to the "mapper")
        // only map if Predicate (digits only) matches the given input
        registrar.withBuilder(
            new Builder<String, Integer>().inputFieldName("userAge")
                                          .outputFieldName("age")
                                          .inputFieldType(String.class)
                                          .outputFieldType(int.class)
                                          .inputPredicate(Pattern.compile("\\d+").asPredicate())
                                          .mapper(Integer::valueOf)
        );

        // 2. color mapping: color to color (String to int, according to the "mapper")
        // only map if output matches the output Predicate (at least 4 digits)

        registrar.withBuilder(
            new Builder<String, Integer>().sameFieldNames("color")
                                          .inputFieldType(String.class)
                                          .outputFieldType(int.class)
                                          .mapper(String::length)
                                          .outputPredicate(x -> x >= 4)
        );

        // 3. map a generic List
        // inputFieldType and outputFieldType both accept a Type
        registrar.withBuilder(
            new Builder<List<Long>, List<Integer>>().sameFieldNames("list")
                                                    .inputFieldType(new TypeToken<List<Integer>>() {}.getType())
                                                    .outputFieldType(List.class)
                                                    .mapper(longList -> longList.stream()
                                                                                .map(Long::intValue)
                                                                                .collect(Collectors.toList()))
        );

        Out<Integer> out = registrar.transform(in);
        System.out.println(out.getAge()); // 57
        System.out.println(out.getColor()); // 4
        System.out.println(out.getList()); // [1]

    }

}
