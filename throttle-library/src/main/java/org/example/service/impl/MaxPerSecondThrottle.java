package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.service.Throttle;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * Реализация интерфейса {@link Throttle}, ограничивающая количество вызовов
 * метода {@code exec} за 1 (одну) секунду.
 * Реализация позволяет вызывать метод {@code exec} без задержек
 * в пределах заданного в конструкторе значения. Данная реализация построена
 * на кольцевом буфере, содержащим метки времени вызовов функции.
 * Данная реализация является не потокобезопасной.
 * @see org.example.service.Throttle
 * @param <T> тип передаваемого и возвращаемого значения функции {@code exec}
 */
@Slf4j
public class MaxPerSecondThrottle<T> implements Throttle<T> {
    private final static Duration ONE_SECOND = Duration.ofSeconds(1);
    private final Instant[] timeStampsRingBuffer;
    private int currentPosition = 0;

    /**
     * Конструктор класса, получающий на вход значение максимального количества
     * вызовов в секунду.
     * @param rateLimit - значение ограничения количества вызовов в секунду
     */
    public MaxPerSecondThrottle(int rateLimit) {
        if (rateLimit <= 0) {
            throw new IllegalArgumentException("Rate limit must be positive number");
        }
        timeStampsRingBuffer = new Instant[rateLimit];
        Arrays.fill(timeStampsRingBuffer, Instant.MIN);
    }

    /**
     * Вызов функции {@code func} с возможной задержкой
     * @param func исполняемая функция, не null
     * @return результат выполнения {@code func}
     */
    @Override
    public T exec(Callable<T> func) {
        if (func == null) {
            throw new IllegalArgumentException("Parameter 'func' must not be null");
        }
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

    /**
     * Проверка количества обращений и, по необходимости, ожидание.
     */
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

    /**
     * Занесение информации выполненном вызове в буфер.
     */
    private void registerCall() {
        timeStampsRingBuffer[currentPosition] = Instant.now();
        currentPosition = (currentPosition + 1) % timeStampsRingBuffer.length;
    }
}