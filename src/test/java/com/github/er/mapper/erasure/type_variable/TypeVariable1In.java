package com.github.er.mapper.erasure.type_variable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class TypeVariable1In<T> {

    private T value;

}
