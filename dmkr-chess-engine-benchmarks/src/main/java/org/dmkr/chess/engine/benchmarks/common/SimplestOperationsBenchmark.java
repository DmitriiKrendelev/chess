package org.dmkr.chess.engine.benchmarks.common;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Measurement(iterations = 3)
@Warmup(iterations = 1)
@BenchmarkMode(value = Mode.AverageTime)
@Fork(value = 3, warmups = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class SimplestOperationsBenchmark {

    @Param({"12345678"})
    public int arg;

    @Benchmark
    public void bitShiftTest(Blackhole blackHole) {
        blackHole.consume(arg >> 4);
    }

    @Benchmark
    public void multiplyTest(Blackhole blackHole) {
        blackHole.consume(arg * 16);
    }


}
