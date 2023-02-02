package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.matchers.GreaterOrEqual;
import org.example.service.Throttle;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class SynchronizedMaxPerSecondThrottleTest {

    @Test
    public void positiveTest() throws InterruptedException {
        Throttle<Integer> testClass = new SynchronizedMaxPerSecondThrottle<>(100);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        List<Callable<Integer>> tasks = IntStream.range(0, 1001)
                .mapToObj((i) -> (Callable<Integer>) () -> {
                    testClass.exec(() -> {
                        System.out.println(i);
                        return null;
                    });
                    return null;
                }).toList();

        Instant startTime = Instant.now();
        List<Future<Integer>> taskResults = threadPool.invokeAll(tasks);
        taskResults.forEach((task) -> {
            try {
                task.get(15, TimeUnit.SECONDS);
            } catch (Exception error) {
                throw new RuntimeException(error);
            }
        });
        Duration testDuration = Duration.between(startTime, Instant.now());
        log.info("MaxPerSecondThrottle test @ 100 cps / 1001 calls = {}", testDuration);

        assertThat(testDuration, new GreaterOrEqual(Duration.ofSeconds(10)));
    }

}