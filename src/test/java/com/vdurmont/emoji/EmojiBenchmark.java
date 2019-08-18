package com.vdurmont.emoji;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xmx2G"})
@Warmup(iterations = 1)
@Measurement(iterations = 5)
public class EmojiBenchmark {

    private List<String> lines = new ArrayList<String>(100000);

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(EmojiBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("100k"));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
//                if (lines.size() > 1000) break;

                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void parseToAliases(Blackhole bh) {
        for (int i = 0; i < lines.size(); i++) {
            String parsed = EmojiParser.parseToAliases(lines.get(i));
            bh.consume(parsed);
        }
    }

    @Benchmark
    public void parseToHtmlDecimal(Blackhole bh) {
        for (int i = 0; i < lines.size(); i++) {
            String parsed = EmojiParser.parseToHtmlDecimal(lines.get(i));
            bh.consume(parsed);
        }
    }

    @Benchmark
    public void parseToHtmlHexadecimal(Blackhole bh) {
        for (int i = 0; i < lines.size(); i++) {
            String parsed = EmojiParser.parseToHtmlHexadecimal(lines.get(i));
            bh.consume(parsed);
        }
    }

    @Benchmark
    public void removeAllEmojis(Blackhole bh) {
        for (int i = 0; i < lines.size(); i++) {
            String parsed = EmojiParser.removeAllEmojis(lines.get(i));
            bh.consume(parsed);
        }
    }

    @Benchmark
    public void parseToUnicode(Blackhole bh) {
        for (int i = 0; i < lines.size(); i++) {
            String parsed = EmojiParser.parseToUnicode(lines.get(i));
            bh.consume(parsed);
        }
    }

}