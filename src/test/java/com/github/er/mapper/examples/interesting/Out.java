package com.github.er.mapper.examples.interesting;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Out<R extends Number> {

    private List<R> list;

    private int age;
    private int color;

}
