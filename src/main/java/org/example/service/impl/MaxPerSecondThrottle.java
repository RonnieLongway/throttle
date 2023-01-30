package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.service.Throttle;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.Callable;

@Slf4j
public class MaxPerSecondThrottle<T> implements Throttle<T> {
    private final static Duration ONE_SECOND = Duration.ofSeconds(1);
    private final Instant[] timeStampsRingBuffer;
    private int currentPosition = 0;

    public MaxPerSecondThrottle(int rateLimit) {
        if (rateLimit <= 0) {
            throw new IllegalArgumentException("Rate limit must be positive number");
        }
        timeStampsRingBuffer = new Instant[rateLimit];
        Arrays.fill(timeStampsRingBuffer, Instant.MIN);
    }

    @Override
    public T exec(Callable<T> func) {
        acquireOrSleep();

        T result;
        try {
            result = func.call();
        } catch (Exception error) {
            log.error("Unexpected exception in callable function. Aborting.", error);
            throw new RuntimeException("Unexpected exception in callable function", error);
        }

        registerCall();
        return result;
    }

    private void acquireOrSleep() {
        Duration deltaTime = Duration.between(timeStampsRingBuffer[currentPosition], Instant.now());
        if (deltaTime.compareTo(ONE_SECOND) < 0) {
            Duration waitPeriod = ONE_SECOND.minus(deltaTime);
            log.debug("Waiting for {}", waitPeriod);
            try {
                Thread.sleep(waitPeriod.toMillis(), waitPeriod.getNano() % 1_000_000);
            } catch (InterruptedException error) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void registerCall() {
        timeStampsRingBuffer[currentPosition] = Instant.now();
        currentPosition = (currentPosition + 1) % timeStampsRingBuffer.length;
    }
}