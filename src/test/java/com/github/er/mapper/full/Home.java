package com.github.er.mapper.full;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Home {

    private String street;
    private int apNumber;
    private Zip zip;

}
