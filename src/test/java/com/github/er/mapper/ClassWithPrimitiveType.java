package com.github.er.mapper;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ClassWithPrimitiveType {

    // primitive type
    private int value;

}
