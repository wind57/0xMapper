package com.github.er.mapper.erasure.array_type;

import com.github.er.mapper.MappingRegistrar;
import com.github.er.mapper.Registrar.Builder;
import com.google.gson.reflect.TypeToken;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayTest {

    // this makes no sense in the scope of the library
    @Test
    public void test1() {

        boolean caught = false;

        try {
            MappingRegistrar<Long[], String[]> reg = new MappingRegistrar<>() {};
        } catch (RuntimeException re) {
            caught = true;
            Assertions.assertEquals("IN/OUT can't be arrays, these are your pojo types", re.getMessage());
        }

        Assertions.assertTrue(caught);

    }

    @Test
    public void test2() {

        MappingRegistrar<ArrIN<Long>, ArrOUT<Integer>> reg = new MappingRegistrar<>() {};

        reg.withBuilder(new Builder<Long[], Integer[]>()
                            .sameFieldNames("genericArray")
                            .inputFieldType(Long[].class)
                            .outputFieldType(Integer[].class)
                            .mapper(in -> Arrays.stream(in).map(Long::intValue).toArray(Integer[]::new))
        );

        reg.withMapping("longArray", Long[].class);

        reg.withBuilder(new Builder<List<Long>[], List<Integer>[]>()
                            .sameFieldNames("listArray")
                            .inputFieldType(new TypeToken<List<Long>[]>() {}.getType())
                            .outputFieldType(new TypeToken<List<Integer>[]>() {}.getType())
                            .mapper(
                                array -> Arrays.stream(array)
                                               .map(list -> list.stream().map(Long::intValue).collect(Collectors.toList()))
                                               .toArray(x -> new List[x])
                            ));

        ArrIN<Long> in = new ArrIN<Long>().setGenericArray(new Long[]{1L})
                                          .setLongArray(new Long[]{1L, 2L})
                                          .setListArray((List<Long>[]) new List[]{List.of(1L)});

        ArrOUT<Integer> out = reg.transform(in);
        Assertions.assertNotNull(out);
        Assertions.assertArrayEquals(new Integer[]{1}, out.getGenericArray());
        Assertions.assertArrayEquals(new Long[]{1L, 2L}, out.getLongArray());

        for (List<Integer> list : out.getListArray()) {
            Assertions.assertIterableEquals(list, List.of(1));
        }

    }

}
