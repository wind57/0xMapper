package com.github.er.mapper.full;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Dev {

    private String name;
    private int age;
    private Color eyesColor;
    private List<String> extraActivities;
    private List<String> children;
    private Address address;
}
