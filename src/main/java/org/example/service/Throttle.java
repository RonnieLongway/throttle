package org.example.service;

import java.util.concurrent.Callable;

public interface Throttle<T> {
    /**
     * @param func исполняемая функция, не null
     * @returns результат выполнения функции
     */
    T exec(Callable<T> func);
}
