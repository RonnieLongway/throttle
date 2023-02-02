package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.matchers.GreaterOrEqual;
import org.example.service.Throttle;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class MaxPerSecondThrottleTest {

    @Test
    public void positiveTest() {
        Throttle<Integer> testClass = new MaxPerSecondThrottle<>(100);

        Instant startTime = Instant.now();
        for (int i = 0; i < 1001; i++) {
            final int j = i;
            testClass.exec(() -> {
                System.out.println(j);
                return null;
            });
        }
        Duration testDuration = Duration.between(startTime, Instant.now());
        log.info("MaxPerSecondThrottle test @ 100 cps / 1001 calls = {}", testDuration);

        assertThat(testDuration, new GreaterOrEqual(Duration.ofSeconds(10)));
    }
}