package org.example.autoconfigure;

import org.example.service.Throttle;
import org.example.service.impl.SynchronizedMaxPerSecondThrottle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * {@link EnableAutoConfiguration Автоконфигуратор}, создающий бин "регулятор" {@link Throttle}
 */
@AutoConfiguration
@ConditionalOnClass(Throttle.class)
@EnableConfigurationProperties(ThrottleProperties.class)
public class ThrottleAutoConfiguration {

    @Autowired
    private ThrottleProperties properties;

    /**
     * Создание конфигурация {@link ThrottleConfig} для последующего
     * создания бина типа {@link Throttle}
     * @return конфигурация {@code ThrottleConfig}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "org.example.throttle", name = "enable",
            havingValue = "true", matchIfMissing = true)
    public ThrottleConfig throttleConfig() {
        ThrottleConfig config = new ThrottleConfig();
        config.setRateLimit(properties.getRateLimit());
        validateConfig(config);
        return config;
    }

    /**
     * Создание бина типа {@link Throttle}
     * @param config конфигурация {@link ThrottleConfig} для создания бина
     * @return бин {@code Throttle}
     * @param <T> возвращаемый тип
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "org.example.throttle", name = "enable",
            havingValue = "true", matchIfMissing = true)
    public <T> Throttle<T> throttle(ThrottleConfig config) {
        validateConfig(config);
        System.out.println("Creating throttle");
        return new SynchronizedMaxPerSecondThrottle<>(config.getRateLimit());
    }

    private void validateConfig(ThrottleConfig config) {
        if (config.getRateLimit() == null || config.getRateLimit() <= 0) {
            throw new IllegalArgumentException("rateLimit must be set and greater than zero");
        }
    }
}
