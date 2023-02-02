# Throttler: регулятор вызова функций
### Краткое описание
Данный репозиторий содержит библиотеку и Spring Boot автоконфигуратор для создания сущностей, 
реализующих механизм "регулятор". Ограничение накладывается на количество вызовов функции.

### Примеры использования
#### Простой пример
Ниже приведен простой пример использования регулятора
```java
public class Example1 {
    //Создаем объект и устанавливаем максимальное количество вызовов
    //функции в секунду равным 100
    Throttle<?> throttle = new MaxPerSecondThrottle<>(100);
    
    void run() {
        //Выполняем вызов функций через регулятор
        for (int i = 0; i < 1001; i++) {
            final int j = i;
            testClass.exec(() -> {
                System.out.println(j);
                return null;
            });
        }
    }
}
```

Для работы этого примера необходимо подключить зависимость
```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>throttle-library</artifactId>
    <version>${throttle.version}</version>
</dependency>
```

#### Spring Boot starter
Теперь приведем пример использования регулятора в Spring Boot приложении
```java
public class Example2 {

    //Внедрение бина регулятора
    @Autowired
    Throttle<?> throttle;
    
    public void run() {
        //Использование бина (см. пример выше)
        throttle.exec( ... );
    }
}
```
Значение максимального количества вызовов в секунду нужно указать в файле ```application.properties```

```properties
org.example.throttle.rateLimit = 100
```

Также регулятор можно сконфигурировать программно:
```java
@Configuration
public class ApplicationConfig {
    @Bean
    public ThrottleConfig myThrottleConfig() {
        ThrottleConfig config = new ThrottleConfig();
        config.setRateLimit(200);
        return config;
    }
}
```

Для отключения создания бина через конфигурацию, необходимо добавить следующее в файл ```application.properties```
```properties
org.example.throttle.enable = false
```

Для работы этого примера необходимо подключить зависимость
```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>throttle-spring-boot-starter</artifactId>
    <version>${throttle-starter.version}</version>
</dependency>
```


### Сборка проекта
Для сборки необходим Apache Maven (контрольная сборка проводилась с использованием версии 3.8.1)
Команда для сборки всех артефактов проекта (jar, javadoc)
```bash
mvn clean package javadoc:jar source:jar
```
