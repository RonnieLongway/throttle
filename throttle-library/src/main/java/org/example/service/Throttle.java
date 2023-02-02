package org.example.service;

import java.util.concurrent.Callable;

/**
 * Интерфейс для создания объектов, реализующих механизм "регулятор".
 * Ограничение накладывается на количество вызовов функции {@code exec}
 * @param <T> - тип передаваемого и возвращаемого из функции значения
 */
public interface Throttle<T> {
    /**
     * @param func исполняемая функция, не null
     * @return результат выполнения функции func
     */
    T exec(Callable<T> func);
}
