package ru.otus.homework;

import java.lang.annotation.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
    class LogInvocationHandler implements InvocationHandler {
        private final Calculable calculable;

        public LogInvocationHandler(Calculable calculable) {
            this.calculable = calculable;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isOverriddenMethodAnnotatedWith(method, Log.class)) {
                System.out.printf("Log pre-processing: %s(%s)%n", method.getName(), Arrays.toString(args));
            }
            return method.invoke(calculable, args);
        }

        private boolean isOverriddenMethodAnnotatedWith(Method method, Class<? extends Annotation> annotation) {
            try {
                return calculable.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes())
                        .isAnnotationPresent(annotation);
            } catch (NoSuchMethodException e) {
                return false;
            }
        }
    }
}
