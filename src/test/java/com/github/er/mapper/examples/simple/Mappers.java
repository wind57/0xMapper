package com.github.er.mapper.examples.simple;

import com.github.er.mapper.MappingRegistrar;
import com.github.roookeee.datus.api.Datus;
import com.github.roookeee.datus.api.Mapper;
import java.util.function.Function;
import org.mapstruct.Mapping;

public class Mappers {

    private static final MappingRegistrar<PersonIn, PersonOut> MAPPER;
    private static final Mapper<PersonIn, PersonOut> DATUS_MAPPER;

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

    @org.mapstruct.Mapper
    interface PersonMapper {

        PersonMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(PersonMapper.class);

        @Mapping(source = "firstName", target = "firstName")
        @Mapping(source = "lastName", target = "lastName")
        PersonOut inToOut(PersonIn in);
    }

    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    private static PersonOut direct(PersonIn in) {
        PersonOut out = new PersonOut();
        out.setFirstName(in.getFirstName());
        out.setLastName(in.getLastName());

        return out;
    }

    public static Function<PersonIn, PersonOut> mapper() {
        return MAPPER::transform;
    }

    public static Function<PersonIn, PersonOut> datusMapper() {
        return DATUS_MAPPER::convert;
    }

    public static Function<PersonIn, PersonOut> directMapper() {
        return Mappers::direct;
    }

    public static Function<PersonIn, PersonOut> mapStructMapper() {
        return PersonMapper.INSTANCE::inToOut;
    }

}
