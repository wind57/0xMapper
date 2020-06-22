package com.github.er.mapper.examples.simple;

import com.github.er.mapper.MappingRegistrar;

public class PersonMapper {

    public static void main(String[] args) {

        PersonIn in = new PersonIn()
            .setFirstName("John")
            .setLastName("Smith");

        MappingRegistrar<PersonIn, PersonOut> mapper = new MappingRegistrar<>(){};
        mapper.withMapping("firstName", String.class)
              .withMapping("lastName", String.class);

        PersonOut out = mapper.transform(in);
        System.out.println(out.getFirstName()); // John
        System.out.println(out.getLastName()); // Smith

    }

}
