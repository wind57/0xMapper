package com.github.er.mapper.parametrized;

import com.github.er.mapper.MappingRegistrar;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParamTest {

    @Test
    public void test() {
        ParamIN paramIN = new ParamIN().setParams(List.of("one", "two"));
        MappingRegistrar<ParamIN, ParamOUT> mappingRegistrar = new MappingRegistrar<>() {};
        mappingRegistrar.directly("params", List.class, ParamIN::getParams);

        ParamOUT paramOUT = mappingRegistrar.transform(paramIN);

        Assertions.assertEquals(List.of("one", "two"), paramOUT.getParams());
    }

}
