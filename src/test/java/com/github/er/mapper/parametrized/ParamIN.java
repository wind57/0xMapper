package com.github.er.mapper.parametrized;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ParamIN {

    private List<String> params;

}
