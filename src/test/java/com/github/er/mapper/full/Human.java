package com.github.er.mapper.full;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class Human {

    private String firstName;
    private String lastName;
    private String age;
    private String eyesColor;
    private List<String> fun;
    private Map<String, String> children;
    private Home home;

}
