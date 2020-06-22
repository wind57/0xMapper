package com.github.er.mapper.types.classtype;

import com.github.er.mapper.MappingRegistrar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ER
 * <p>
 * a test when Type == Class
 */
public class ClassTypeTest {

    @Test
    public void test() {

        In in = new In().setAge(12).setName("ab");

        MappingRegistrar<In, Out> registrar = new MappingRegistrar<>() {};
        registrar.withMapping("name", String.class);
        registrar.withMapping("age", int.class);
        Out out = registrar.transform(in);

        Assertions.assertNotNull(out);
        Assertions.assertEquals("ab", out.getName());
        Assertions.assertSame(12, out.getAge());

    }

}

