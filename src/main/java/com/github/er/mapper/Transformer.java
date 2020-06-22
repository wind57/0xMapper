package com.github.er.mapper;

/**
 * @author ER
 */
public interface Transformer<INPUT, OUTPUT> {

    OUTPUT transform(INPUT input);

}
