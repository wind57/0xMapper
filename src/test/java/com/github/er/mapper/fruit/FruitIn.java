package com.github.er.mapper.fruit;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class FruitIn {

    private String name;
    private String color;
    private String shape;

}
