package com.github.er.mapper.software;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SoftwareDev {

    private String firstname;
    private String occupation;
    // yeah, long is too much, but just to prove that the test is fine
    private long age;

}
