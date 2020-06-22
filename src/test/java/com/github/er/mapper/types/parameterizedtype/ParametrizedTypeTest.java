package com.github.er.mapper.types.parameterizedtype;

import com.github.er.mapper.MappingRegistrar;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParametrizedTypeTest {

    @Test
    public void test() {

        In in = new In().setList(List.of("one"));
        MappingRegistrar<In, Out> registrar = new MappingRegistrar<>() {};

        registrar.withMapping("list", TypeToken.getParameterized(List.class, String.class).getType());

        Out out = registrar.transform(in);

        Assertions.assertNotNull(out);
        Assertions.assertEquals(List.of("one"), out.getList());

    }


}
