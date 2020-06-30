package com.github.er.mapper;

import com.github.er.mapper.examples.interesting.In;
import com.github.er.mapper.examples.interesting.Mappers;
import com.github.er.mapper.examples.interesting.Out;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 5)
@Measurement(iterations = 10, time = 5)
@BenchmarkMode(Mode.AverageTime)
public class InterestingExampleTest {

    public static void runInteresting(String version) throws Exception {
        Options opt = new OptionsBuilder()
            .include(InterestingExampleTest.class.getSimpleName())
            .resultFormat(ResultFormatType.TEXT)
            .result("interesting-bench-" + version + ".txt")
            .jvmArgs("-Xmx10g", "-Xms10g")
            .build();

        new Runner(opt).run();
    }


    public In<Long> in;

    @Setup(value = Level.Invocation)
    public void setUp() {

        // I don't care about the contents of these
        String userAge = ThreadLocalRandom.current().nextInt() + "";
        String color = ThreadLocalRandom.current().ints().limit(5).mapToObj(String::valueOf).collect(Collectors.joining());
        long l = ThreadLocalRandom.current().nextLong();

        in = new In<Long>()
            .setList(List.of(l))
            .setUserAge(userAge)
            .setColor(color);
    }

    // --------------------------------------------------------------------------------------

    @Benchmark
    @Fork(value = 1)
    public Out<Integer> direct() {
        return Mappers.directMapper().apply(in);
    }

    @Benchmark
    @Fork(value = 1)
    public Out<Integer> mapper() {
        return Mappers.mapper().apply(in);
    }

    @Benchmark
    @Fork(value = 1)
    public Out<Integer> mapStruct() {
        return Mappers.mapStructMapper().apply(in);
    }

    @Benchmark
    @Fork(value = 1)
    public Out<Integer> datus() {
        return Mappers.datusMapper().apply(in);
    }
}
