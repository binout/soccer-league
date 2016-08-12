package io.github.binout.soccer.infrastructure.log;

public class LoggerService {

    public void log(Class<?> clazz, String message) {
        System.out.println(clazz.getName() + " : " + message);
    }
}
