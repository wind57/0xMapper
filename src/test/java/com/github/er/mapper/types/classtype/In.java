package com.github.er.mapper.types.classtype;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class In {

    private String name;
    private int age;

}
