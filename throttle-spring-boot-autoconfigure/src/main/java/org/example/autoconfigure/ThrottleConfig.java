package org.example.autoconfigure;

import lombok.Data;

/**
 * Класс для хранения конфигурации при создании бина типа "регулятор"
 *
 * @see ThrottleAutoConfiguration
 */
@Data
public class ThrottleConfig {
    /**
     * Значение ограничения количества вызовов в секунду
     */
    private Integer rateLimit;
}
