package org.example.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for Throttle auto-configurator
 *
 * @see ThrottleAutoConfiguration
 * @see ThrottleConfig
 */
@ConfigurationProperties("org.example.throttle")
@Data
public class ThrottleProperties {
    /**
     * Значение ограничения количества вызовов в секунду
     */
    private Integer rateLimit;
}
