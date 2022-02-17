package ru.otus.homework;

import java.lang.annotation.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
    class LogInvocationHandler implements InvocationHandler {
        private final Calculable calculable;
        private final Set<Method> interfaceMethodsToProcess;

        public LogInvocationHandler(Calculable calculable) {
            this.calculable = calculable;
            this.interfaceMethodsToProcess = getInterfaceMethodsToProcess();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (interfaceMethodsToProcess.contains(method)) {
                System.out.printf("executed method: %s, param: %s%n", method.getName(), Arrays.toString(args));
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

        private Set<Method> getInterfaceMethodsToProcess() {
            return Arrays.stream(calculable.getClass().getDeclaredMethods())
                    .filter(method -> isOverriddenMethodAnnotatedWith(method, Log.class))
                    .map(this::getInterfaceMethodFromImpl)
                    .collect(Collectors.toSet());
        }

        private Method getInterfaceMethodFromImpl(Method implMethod) {
            try {
                return Calculable.class.getDeclaredMethod(implMethod.getName(), implMethod.getParameterTypes());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
