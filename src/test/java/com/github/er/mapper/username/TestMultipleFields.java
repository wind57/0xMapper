package com.github.er.mapper.username;

import com.github.er.mapper.MappingRegistrar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMultipleFields {

    @Test
    public void testMultipleFields() {

        UserIn in = new UserIn().setFirstName("Eugene")
                                .setLastName("Rabii");

        MappingRegistrar<UserIn, UserOut> registrar = new MappingRegistrar<>() {};
        registrar.directly(
            "nickName", String.class,
            x -> x.getFirstName().substring(0, 1) + x.getLastName().substring(0, 1)
        );

        UserOut out = registrar.transform(in);
        Assertions.assertNotNull(out);
        Assertions.assertEquals("ER", out.getNickName());

    }

}
