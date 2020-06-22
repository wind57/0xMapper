package com.github.er.mapper.erasure.type_variable;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeVariable12Out<T extends Number & Comparable> {

    private T value;

}

