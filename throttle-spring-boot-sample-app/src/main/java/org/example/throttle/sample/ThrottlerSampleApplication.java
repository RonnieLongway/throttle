package org.example.throttle.sample;

import lombok.extern.slf4j.Slf4j;
import org.example.autoconfigure.ThrottleConfig;
import org.example.service.Throttle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.Instant;

/**
 * Тестовое приложение, демонстрирующее применение "регулятора" в прикладном коде
 */
@SpringBootApplication
@Slf4j
public class ThrottlerSampleApplication implements CommandLineRunner {

    @Autowired
    private Throttle<String> throttle;

    /**
     * Функция main приложения
     * @param args параметры командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(ThrottlerSampleApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        log.info("Running throttling test");

        Instant startTime = Instant.now();
        for (int i = 0; i < 1001; i++) {
            final int j = i;
            String result = throttle.exec(() -> "@" + j);
            log.info("result = {}", result);
        }
        Duration result = Duration.between(startTime, Instant.now());

        log.info("Total duration @ 100 cps / 1001 calls = {}", result);
    }
}
