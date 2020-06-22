package com.github.er.mapper.student;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Human {

    private String name;
    private int age;

}
