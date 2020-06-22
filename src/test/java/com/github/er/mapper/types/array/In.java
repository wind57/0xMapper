package com.github.er.mapper.types.array;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class In<T> {

    String[] elements;

}
