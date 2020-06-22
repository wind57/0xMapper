package com.github.er.mapper.user;

import com.github.er.mapper.MappingRegistrar;
import com.github.er.mapper.Registrar.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testWithFunction() {

        UserIn in = new UserIn().setAge("35");
        MappingRegistrar<UserIn, UserOut> mappingRegistrar = new MappingRegistrar<>() {};

        mappingRegistrar.withBuilder(new Builder<String, Integer>()
                                         .inputFieldName("age")
                                         .outputFieldName("years")
                                         .inputFieldType(String.class)
                                         .outputFieldType(int.class)
                                         .mapper(Integer::parseInt));

        UserOut out = mappingRegistrar.transform(in);
        Assertions.assertNotNull(out);
        Assertions.assertSame(35, out.getYears());

    }

    @Test
    public void testWithFunctionAndPredicateMatch() {

        UserIn in = new UserIn().setAge("35");
        MappingRegistrar<UserIn, UserOut> mappingRegistrar = new MappingRegistrar<>() {};

        mappingRegistrar.withBuilder(new Builder<String, Integer>()
                                         .inputFieldName("age")
                                         .outputFieldName("years")
                                         .inputFieldType(String.class)
                                         .outputFieldType(int.class)
                                         .inputPredicate(x -> x.contains("35"))
                                         .mapper(Integer::parseInt));

        UserOut out = mappingRegistrar.transform(in);
        Assertions.assertNotNull(out);
        Assertions.assertSame(35, out.getYears());

    }

    @Test
    public void testWithFunctionAndPredicateNOMatch() {

        UserIn in = new UserIn().setAge("35");
        MappingRegistrar<UserIn, UserOut> mappingRegistrar = new MappingRegistrar<>() {};

        mappingRegistrar.withBuilder(new Builder<String, Integer>()
                                         .inputFieldName("age")
                                         .outputFieldName("years")
                                         .inputFieldType(String.class)
                                         .outputFieldType(int.class)
                                         .inputPredicate("50"::equals)
                                         .mapper(Integer::parseInt));

        UserOut out = mappingRegistrar.transform(in);
        Assertions.assertNotNull(out);
        Assertions.assertSame(0, out.getYears());

    }

}
