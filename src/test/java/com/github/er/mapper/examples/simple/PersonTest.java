package com.github.er.mapper.examples.simple;

import com.github.er.mapper.MappingRegistrar;
import com.github.roookeee.datus.api.Datus;
import com.github.roookeee.datus.api.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PersonTest {

    public static final MappingRegistrar<PersonIn, PersonOut> MAPPER;
    public static final Mapper<PersonIn, PersonOut> DATUS_MAPPER;
    private final PersonIn in = new PersonIn()
        .setFirstName("John")
        .setLastName("Smith");

    static {
        MAPPER = new MappingRegistrar<>() {};
        MAPPER.withMapping("firstName", String.class)
              .withMapping("lastName", String.class);

        DATUS_MAPPER = Datus.forTypes(PersonIn.class, PersonOut.class)
                            .mutable(PersonOut::new)
                            .from(PersonIn::getFirstName).into(PersonOut::setFirstName)
                            .from(PersonIn::getLastName).into(PersonOut::setLastName)
                            .build();
    }

    private void assertions(PersonOut out) {
        Assertions.assertNotNull(out);
        Assertions.assertEquals("John", out.getFirstName());
        Assertions.assertEquals("Smith", out.getLastName());
    }

    @Test
    public void testSimpleWithMapper() {

        PersonOut out = MAPPER.transform(in);
        assertions(out);

    }

    @Test
    public void testSimpleWithDirect() {

        PersonOut out = new PersonOut();
        out.setLastName(in.getLastName());
        out.setFirstName(in.getFirstName());
        assertions(out);

    }

    @Test
    public void testSimpleWithDatus() {

        PersonOut out = DATUS_MAPPER.convert(in);
        assertions(out);

    }

    @Test
    public void testSimpleWithMapStruct() {
        PersonOut out = PersonMapper.INSTANCE.inToOut(in);
        assertions(out);
    }

}
