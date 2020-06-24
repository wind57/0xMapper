package com.github.er.mapper.examples.interesting;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InterestingTest {

    private final In<Long> in1 = new In<Long>()
        .setList(List.of(1L))
        .setUserAge("57")
        .setColor("blue");

    // age not mapped because of "57d"
    private final In<Long> in2 = new In<Long>()
        .setList(List.of(1L))
        .setUserAge("57d")
        .setColor("blue");

    // age not mapped because of "red" (at least 4 is not met)
    private final In<Long> in3 = new In<Long>()
        .setList(List.of(1L))
        .setUserAge("57")
        .setColor("red");

    @Test
    public void testInteresting1WithMapper() {
        Out<Integer> out = Mappers.mapper().apply(in1);
        assertions1(out);
    }

    @Test
    public void testInteresting1WithDatusMapper() {
        Out<Integer> out = Mappers.datusMapper().apply(in1);
        assertions1(out);
    }

    @Test
    public void testInteresting1WithDirectMapper() {
        Out<Integer> out = Mappers.directMapper().apply(in1);
        assertions1(out);
    }

    @Test
    public void testInteresting1WithmapStructMapper() {
        Out<Integer> out = Mappers.mapStructMapper().apply(in1);
        assertions1(out);
    }

    // -------------------------------------------------------------------

    @Test
    public void testInteresting2WithMapper() {
        Out<Integer> out = Mappers.mapper().apply(in2);
        assertions2(out);
    }

    @Test
    public void testInteresting2WithDatusMapper() {
        Out<Integer> out = Mappers.datusMapper().apply(in2);
        assertions2(out);
    }

    @Test
    public void testInteresting2WithDirectMapper() {
        Out<Integer> out = Mappers.directMapper().apply(in2);
        assertions2(out);
    }

    @Test
    public void testInteresting2WithmapStructMapper() {
        Out<Integer> out = Mappers.mapStructMapper().apply(in2);
        assertions2(out);
    }

    // -------------------------------------------------------------------

    @Test
    public void testInteresting3WithMapper() {
        Out<Integer> out = Mappers.mapper().apply(in3);
        assertions3(out);
    }

    @Test
    public void testInteresting3WithDatusMapper() {
        Out<Integer> out = Mappers.datusMapper().apply(in3);
        assertions3(out);
    }

    @Test
    public void testInteresting3WithDirectMapper() {
        Out<Integer> out = Mappers.directMapper().apply(in3);
        assertions3(out);
    }

    @Test
    public void testInteresting3WithmapStructMapper() {
        Out<Integer> out = Mappers.mapStructMapper().apply(in3);
        assertions3(out);
    }

    // happy path case
    private void assertions1(Out<Integer> out) {
        Assertions.assertNotNull(out);
        Assertions.assertEquals(57, out.getAge());
        Assertions.assertEquals(4, out.getColor());
        Assertions.assertIterableEquals(List.of(1), out.getList());
    }

    private void assertions2(Out<Integer> out) {
        Assertions.assertNotNull(out);
        Assertions.assertEquals(0, out.getAge()); // this!
        Assertions.assertEquals(4, out.getColor());
        Assertions.assertIterableEquals(List.of(1), out.getList());
    }

    private void assertions3(Out<Integer> out) {
        Assertions.assertNotNull(out);
        Assertions.assertEquals(57, out.getAge());
        Assertions.assertEquals(0, out.getColor()); // this
        Assertions.assertIterableEquals(List.of(1), out.getList());
    }
}
