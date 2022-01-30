import annotation.After;
import annotation.Before;
import annotation.Test;
import util.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestExecutor {
    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";

    public static void runTestClass(Class<?> clazz) {
        Method before = null;
        Method after = null;
        ArrayList<Method> tests = new ArrayList<>();
        var results = initResultMap();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                tests.add(method);
            } else if (method.isAnnotationPresent(Before.class)) {
                before = method;
            } else if (method.isAnnotationPresent(After.class)) {
                after = method;
            }
        }

        for (Method testMethod : tests) {
            runTest(clazz, before, testMethod, after, results);
        }

        System.out.printf("Results: Total[%s], Success[%s], Failed[%s]%n", tests.size(), results.get(SUCCESS), results.get(FAILED));
    }

    private static void runTest(Class<?> testClass, Method before, Method testMethod, Method after, Map<String, Integer> results) {
        Object testInstance = null;
        try {
            testInstance = ReflectionHelper.instantiate(testClass);
            ReflectionHelper.callMethod(testInstance, before.getName());
            ReflectionHelper.callMethod(testInstance, testMethod.getName());
            System.out.println("Test successfully finished: " + testMethod.getName());
            results.computeIfPresent(SUCCESS, (k, v) -> ++v);
        } catch (Exception e) {
            System.out.println("Error during test execution: " + testMethod.getName());
            results.computeIfPresent(FAILED, (k, v) -> ++v);
        } finally {
            ReflectionHelper.callMethod(testInstance, after.getName());
        }
    }

    private static Map<String, Integer> initResultMap() {
        Map<String, Integer> results = new HashMap<>();
        results.put(SUCCESS, 0);
        results.put(FAILED, 0);
        return results;
    }
}
