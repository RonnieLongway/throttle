package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.service.Throttle;
import org.example.service.impl.MaxPerSecondThrottle;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class Main {
    public static void main(String[] args) {
        System.out.println("Running throttling test");
        Throttle<Integer> throttle = new MaxPerSecondThrottle<>(100);
        Instant startTime = Instant.now();
        for (int i = 0; i < 1001; i++) {
            final int j = i;
            throttle.exec(() -> {
                System.out.println(j);
                return null;
            });
        }
        Duration result = Duration.between(startTime, Instant.now());
        log.info("Total duration @ 100 cps / 1001 calls = {}", result);
    }
}