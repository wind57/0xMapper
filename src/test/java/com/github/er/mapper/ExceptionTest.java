package com.github.er.mapper;

import com.github.er.mapper.Registrar.Builder;
import com.github.er.mapper.exception.MethodNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ER
 * some tests that prove the correctness of some exceptions or some error messages
 * that would make sense to the callers
 */
public class ExceptionTest {

    @Test
    public void testObjectMethodNotFound() {

        MappingRegistrar<ClassWithPrimitiveType, ClassWithPrimitiveType> registrar = new MappingRegistrar<>() {};

        boolean caught = false;

        try {

            registrar.withMapping("value", Object.class);

        } catch (MethodNotFoundException methodNotFoundException) {
            caught = true;
            Assertions.assertEquals(
                "Class : 'ClassWithPrimitiveType' has no method : 'getValue' that returns : 'Object'",
                methodNotFoundException.getMessage()
            );
        }

        Assertions.assertTrue(caught);

    }

    @Test
    public void testBoxedMethodNotFound() {

        MappingRegistrar<ClassWithPrimitiveType, ClassWithPrimitiveType> registrar = new MappingRegistrar<>() {};

        boolean caught = false;

        try {

            registrar.withMapping("value", Integer.class);

        } catch (MethodNotFoundException methodNotFoundException) {
            caught = true;
            System.out.println(methodNotFoundException.getMessage());
            Assertions.assertEquals(
                "Class : 'ClassWithPrimitiveType' has no method : 'getValue' that returns : 'Integer'",
                methodNotFoundException.getMessage()
            );
        }

        Assertions.assertTrue(caught);

    }

    // no mapping is provided
    @Test
    public void testBuilder() {

        MappingRegistrar<BuilderIn, BuilderOut> registrar = new MappingRegistrar<>() {};

        boolean caught = false;

        try {
            registrar.withBuilder(new Builder<>().inputFieldName("name")
                                                 .outputFieldName("nameLength")
                                                 .inputFieldType(String.class)
                                                 .outputFieldType(long.class)
                                                 .inputPredicate("John"::equals));
        } catch (NullPointerException e) {
            caught = true;
            Assertions.assertEquals(
                "there are properties you need to set when using a Builder: inField, outField, inType, outType, mapper. If you do not require all these, you can use 'withMapping' overloads.",
                e.getMessage());
        }

        Assertions.assertTrue(caught);

    }

}
