package com.github.er.mapper.types.array;

import com.github.er.mapper.MappingRegistrar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayTest {

    @Test
    public void testElements() {

        MappingRegistrar<In<String>, Out> registrar = new MappingRegistrar<>() {};
        registrar.withMapping("elements", String[].class);

        In<String> in = new In<String>().setElements(new String[]{"one"});
        Out out = registrar.transform(in);

        Assertions.assertNotNull(out);
        Assertions.assertArrayEquals(new String[]{"one"}, out.getElements());

    }

}
