package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        checkConfigClass(initialConfigClass);
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        Arrays.stream(initialConfigClasses)
                .peek(this::checkConfigClass)
                .sorted(Comparator.comparingInt(cl -> cl.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEachOrdered(this::processConfig);
    }

    public AppComponentsContainerImpl(String packageName) {
        this(getConfigClassesFromPackage(packageName));
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var components = appComponents.stream()
                .filter(componentClass::isInstance)
                .map(c -> (C) c)
                .toList();

        if (components.isEmpty()) {
            throw new RuntimeException(
                    String.format(
                            "There are no components of %s",
                            componentClass.getName())
            );
        } else if (components.size() > 1) {
            throw new RuntimeException(
                    String.format(
                            "There are %s components of the class %s: %s",
                            components.size(),
                            componentClass.getName(),
                            components
                    )
            );
        }

        return components.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (!appComponentsByName.containsKey(componentName)) {
            throw new RuntimeException(
                    String.format(
                            "There are no components with name %s",
                            componentName)
            );
        }

        return (C) appComponentsByName.get(componentName);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private void processConfig(Class<?> configClass) {
        Object config = createConfig(configClass);

        Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .forEachOrdered(method -> processComponent(method, config));
    }

    public Object createConfig(Class<?> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processComponent(Method method, Object context) {
        String name = method.getAnnotation(AppComponent.class).name();
        checkIfComponentWithNameExists(name);

        Object component = createComponent(method, context);

        appComponents.add(component);
        appComponentsByName.put(name, component);
    }

    private Object createComponent(Method method, Object context) {
        try {
            Object[] params = Arrays.stream(method.getParameterTypes())
                    .map(this::getAppComponent)
                    .toArray();
            return method.invoke(context, params);
        } catch (Exception e) {
            throw new RuntimeException("Error during component initialization", e);
        }
    }

    private void checkIfComponentWithNameExists(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("AppComponent name is null");
        }

        if (appComponentsByName.containsKey(name)) {
            throw new RuntimeException(
                    String.format(
                            "Component with the name %s already exists: %s",
                            name,
                            appComponentsByName.get(name)
                    )
            );
        }
    }

    private static Class<?>[] getConfigClassesFromPackage(String packageName) {
        var configClasses = new Reflections(packageName)
                .getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .toArray(Class<?>[]::new);

        if (configClasses.length == 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "No classes annotated with %s were found in %s package",
                            AppComponentsContainerConfig.class,
                            packageName
                    )
            );
        }

        return configClasses;
    }
}
