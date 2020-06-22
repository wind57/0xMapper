package com.github.er.mapper.erasure.type_variable;

import com.github.er.mapper.MappingRegistrar;
import com.github.er.mapper.Registrar.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ER
 */
public class TypeVariableTest {

    // both In and Out have a type parameter "T"
    @Test
    public void test1() {

        TypeVariable1In<String> in = new TypeVariable1In<String>().setValue("str");

        MappingRegistrar<TypeVariable1In<String>, TypeVariable1Out<String>> reg = new MappingRegistrar<>() {};

        // the erased value of 'T' is Object, so we should really be passing a Object.class in here
        // but this is more idiomatic to the callers (String.class). As such this case is handled in :
        // GetterErasureTypeVariable && SetterErasureTypeVariable
        TypeVariable1Out<String> out = reg.withMapping("value", String.class).transform(in);

        Assertions.assertNotNull(out);
        Assertions.assertEquals("str", out.getValue());

    }

    // IN has a type parameter (T), OUT has a bounded type parameter (T extends Number)
    @Test
    public void test2() {

        TypeVariable1In<String> in = new TypeVariable1In<String>().setValue("57");

        MappingRegistrar<TypeVariable1In<String>, TypeVariable11Out<Integer>> reg = new MappingRegistrar<>() {};

        // the erased value of 'T extends Number' is Number, so we should really be passing a Number.class in here
        // but this is more idiomatic to the callers (Integer.class). As such this case is handled in :
        // GetterErasureTypeVariable && SetterErasureTypeVariable
        reg.withBuilder(
            new Builder<String, Integer>().sameFieldNames("value")
                                          .inputFieldType(String.class)
                                          .outputFieldType(Integer.class)
                                          .mapper(Integer::valueOf));

        TypeVariable11Out<Integer> out = reg.transform(in);

        Assertions.assertNotNull(out);
        Assertions.assertEquals(57, out.getValue());

    }

    // outputFieldType(Object.class) can't work, as 'T extends Number' is erased to Number
    @Test
    public void test3() {

        boolean caught = false;

        try {

            MappingRegistrar<TypeVariable1In<String>, TypeVariable11Out<Integer>> reg = new MappingRegistrar<>() {};

            reg.withBuilder(
                new Builder<String, Integer>().sameFieldNames("value")
                                              .inputFieldType(String.class)
                                              .outputFieldType(Object.class)
                                              .mapper(Integer::valueOf));

        } catch (RuntimeException ncme) {
            caught = true;
            System.out.println(ncme.getMessage());
        }

        Assertions.assertTrue(caught);
    }

    // IN has a type parameter (T), OUT has a bounded type parameter (T extends Number)
    @Test
    public void test4() {

        TypeVariable1In<String> in = new TypeVariable1In<String>().setValue("57");

        MappingRegistrar<TypeVariable1In<String>, TypeVariable12Out<Integer>> reg = new MappingRegistrar<>() {};

        // the erased value of 'T extends Number & Comparable' is Number, so we should really be passing a Number.class in here
        // but this is more idiomatic to the callers (Integer.class). As such this case is handled in :
        // GetterErasureTypeVariable && SetterErasureTypeVariable
        reg.withBuilder(
            new Builder<String, Integer>().sameFieldNames("value")
                                          .inputFieldType(String.class)
                                          .outputFieldType(Integer.class)
                                          .mapper(Integer::valueOf));

        TypeVariable12Out<Integer> out = reg.transform(in);

        Assertions.assertNotNull(out);
        Assertions.assertEquals(57, out.getValue());

    }

}
