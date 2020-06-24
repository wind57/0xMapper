package com.github.er.mapper.examples.simple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleTest {

    private final PersonIn in = new PersonIn()
        .setFirstName("John")
        .setLastName("Smith");

    private void assertions(PersonOut out) {
        Assertions.assertNotNull(out);
        Assertions.assertEquals("John", out.getFirstName());
        Assertions.assertEquals("Smith", out.getLastName());
    }

    @Test
    public void testSimpleWithMapper() {

        PersonOut out = Mappers.mapper().apply(in);
        assertions(out);

    }

    @Test
    public void testSimpleWithDirect() {

        PersonOut out = Mappers.directMapper().apply(in);
        assertions(out);

    }

    @Test
    public void testSimpleWithDatus() {

        PersonOut out = Mappers.datusMapper().apply(in);
        assertions(out);

    }

    @Test
    public void testSimpleWithMapStruct() {
        PersonOut out = Mappers.mapStructMapper().apply(in);
        assertions(out);
    }

}
