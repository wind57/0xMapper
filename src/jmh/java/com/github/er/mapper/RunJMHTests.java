package com.github.er.mapper;

import java.util.stream.IntStream;

public class RunJMHTests {

    public static void main(String[] args) throws Exception {
        int i = IntStream.range(0, args.length)
                         .filter(x -> "version".equals(args[x]))
                         .findFirst()
                         .orElseThrow(() -> new RuntimeException("version not provided"));

        SimpleExampleTest.runSimple(args[i + 1]);
        //InterestingExampleTest.runInteresting(args[i + 1]);
    }

}
