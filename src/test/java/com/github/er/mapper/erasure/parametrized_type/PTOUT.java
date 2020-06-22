package com.github.er.mapper.erasure.parametrized_type;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PTOUT<T extends CharSequence> {

    private List<T> values;

}
