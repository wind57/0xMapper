package com.github.er.mapper.types.parameterizedtype;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class In {

    private List<String> list;

}
