package com.github.er.mapper.erasure.array_type;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArrOUT<T extends Number> {

    T[] genericArray;
    Long[] longArray;
    List<T>[] listArray;

}
