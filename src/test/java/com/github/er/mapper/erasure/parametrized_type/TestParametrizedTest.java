package com.github.er.mapper.erasure.parametrized_type;

import com.github.er.mapper.MappingRegistrar;
import com.github.er.mapper.Registrar.Builder;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestParametrizedTest {

    @Test
    public void test1() {

        MappingRegistrar<PTIN<Integer>, PTOUT<String>> reg = new MappingRegistrar<>() {};

        reg.withBuilder(new Builder<List<Integer>, List<String>>()
                            .sameFieldNames("values")
                            .inputFieldType(new TypeToken<List<Integer>>() {}.getType())
                            .outputFieldType(new TypeToken<List<String>>() {}.getType())
                            .mapper(list -> list.stream().map(x -> "" + x).collect(Collectors.toList()))
        );

        PTOUT<String> result = reg.transform(new PTIN<Integer>().setValues(List.of(1)));
        Assertions.assertNotNull(result);
        Assertions.assertIterableEquals(List.of("1"), result.getValues());

    }

    // same test as above, but List.class given as input
    @Test
    public void test2() {

        MappingRegistrar<PTIN<Integer>, PTOUT<String>> reg = new MappingRegistrar<>() {};

        reg.withBuilder(new Builder<List<Integer>, List<String>>()
                            .sameFieldNames("values")
                            .inputFieldType(List.class)
                            .outputFieldType(List.class)
                            .mapper(list -> list.stream().map(x -> "" + x).collect(Collectors.toList()))
        );

        PTOUT<String> result = reg.transform(new PTIN<Integer>().setValues(List.of(1)));
        Assertions.assertNotNull(result);
        Assertions.assertIterableEquals(List.of("1"), result.getValues());

    }

}
