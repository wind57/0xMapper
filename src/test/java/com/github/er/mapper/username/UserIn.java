package com.github.er.mapper.username;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserIn {

    private String firstName;
    private String lastName;

}
