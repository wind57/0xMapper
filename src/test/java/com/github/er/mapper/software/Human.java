package com.github.er.mapper.software;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Human {

    private String name;
    private String occupation;
    private int age;

}
