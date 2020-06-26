package com.github.er.mapper;

import com.github.er.mapper.examples.simple.Mappers;
import com.github.er.mapper.examples.simple.PersonIn;
import com.github.er.mapper.examples.simple.PersonOut;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 5)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class SimpleExampleTest {

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
            .include(SimpleExampleTest.class.getSimpleName())
            .build();

        new Runner(opt).run();
    }

    public PersonIn personIn;

    @Setup(value = Level.Iteration)
    public void setUp() {

        // I don't care about the contents of these really
        String firstName = ThreadLocalRandom.current().nextInt() + "";
        String lastName = ThreadLocalRandom.current().nextInt() + "";

        personIn = new PersonIn().setFirstName(firstName)
                                 .setLastName(lastName);
    }

    // --------------------------------------------------------

    @Benchmark
    @Fork(value = 1)
    public PersonOut direct() {
        return Mappers.directMapper().apply(personIn);
    }

    @Benchmark
    @Fork(value = 1)
    public PersonOut mapper() {
        return Mappers.mapper().apply(personIn);
    }

    @Benchmark
    @Fork(value = 1)
    public PersonOut mapStruct() {
        return Mappers.mapStructMapper().apply(personIn);
    }

    @Benchmark
    @Fork(value = 1)
    public PersonOut datus() {
        return Mappers.datusMapper().apply(personIn);
    }
}