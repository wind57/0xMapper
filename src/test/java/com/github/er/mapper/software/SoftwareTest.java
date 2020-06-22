package com.github.er.mapper.software;

import com.github.er.mapper.MappingRegistrar;
import com.github.er.mapper.Registrar.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SoftwareTest {

    @Test
    public void test() {

        Human h = new Human()
            .setAge(35)
            .setName("ER")
            .setOccupation("SoftwareDev");

        MappingRegistrar<Human, SoftwareDev> registrar = new MappingRegistrar<>() {};
        registrar.withMapping("name", "firstname", String.class);
        registrar.withMapping("occupation", String.class);

        registrar.withBuilder(new Builder<Integer, Long>().sameFieldNames("age")
                                                          .inputFieldType(int.class)
                                                          .outputFieldType(long.class)
                                                          .mapper(Integer::longValue));

        SoftwareDev dev = registrar.transform(h);

        Assertions.assertEquals("ER", dev.getFirstname());
        Assertions.assertSame(35L, dev.getAge());
        Assertions.assertEquals("SoftwareDev", dev.getOccupation());

    }

}
