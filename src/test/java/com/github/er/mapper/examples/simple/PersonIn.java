package com.github.er.mapper.examples.simple;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PersonIn {
    // getters/setters + constructor omitted
    private String firstName;
    private String lastName;

}