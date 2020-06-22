package com.github.er.mapper.erasure.array_type;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ArrIN<T> {

    T[] genericArray;
    Long[] longArray;
    List<T>[] listArray;

}
