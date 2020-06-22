package com.github.er.mapper.erasure.parametrized_type;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class PTIN<T> {

    private List<T> values;

}
