package com.github.er.mapper.examples.interesting;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class In<T extends Number> {

    private List<T> list;

    private String userAge;
    private String color;

}
