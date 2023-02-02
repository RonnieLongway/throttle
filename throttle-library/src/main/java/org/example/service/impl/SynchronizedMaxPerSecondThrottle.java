package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.service.Throttle;

import java.util.concurrent.Callable;

/**
 * Реализация интерфейса {@link Throttle}, ограничивающая количество вызовов
 * метода {@code exec} за 1 (одну) секунду.
 * Реализация является потокобезопасным вариантом реализации {@link MaxPerSecondThrottle}.
 * @see Throttle
 * @param <T> тип передаваемого и возвращаемого значения функции {@code exec}
 */
@Slf4j
public class SynchronizedMaxPerSecondThrottle<T> extends MaxPerSecondThrottle<T> {

    /**
     * Конструктор класса, получающий на вход значение максимального количества
     * вызовов в секунду.
     * @param rateLimit - значение ограничения количества вызовов в секунду
     */
    public SynchronizedMaxPerSecondThrottle(int rateLimit) {
        super(rateLimit);
    }

    /**
     * Вызов функции {@code func} с возможной задержкой
     * @param func исполняемая функция, не null
     * @return результат выполнения {@code func}
     */
    @Override
    public synchronized T exec(Callable<T> func) {
        return super.exec(func);
    }
}