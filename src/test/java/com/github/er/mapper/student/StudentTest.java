package com.github.er.mapper.student;

import com.github.er.mapper.MappingRegistrar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ER
 */
public class StudentTest {

    /**
     * a very simple Test from {@link Human} to {@link Student}
     */
    @Test
    public void testMappingSingleField() {
        MappingRegistrar<Human, Student> registrar = new MappingRegistrar<>() {};
        registrar.withMapping("name", String.class);
        Student s = registrar.transform(new Human().setName("ER"));

        Assertions.assertEquals("ER", s.getName());
        Assertions.assertSame(0, s.getAge()); // default value
    }

    @Test
    public void testMapping() {
        MappingRegistrar<Human, Student> registrar = new MappingRegistrar<>() {};
        registrar.withMapping("name", String.class);
        registrar.withMapping("age", int.class);
        Student s = registrar.transform(new Human().setName("ER").setAge(35));

        Assertions.assertEquals("ER", s.getName());
        Assertions.assertSame(35, s.getAge());
    }

}
