import annotation.After;
import annotation.Before;
import annotation.Test;
import util.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestExecutor {

    public void runTestClass(Class<?> clazz) {
        final var info = parseTestClass(clazz);

        info.tests.forEach(test -> runTest(clazz, test, info));

        System.out.println(info);
    }

    private void runTest(Class<?> testClass, Method testMethod, TestInfo info) {
        Object testInstance = null;
        try {
            testInstance = ReflectionHelper.instantiate(testClass);
            ReflectionHelper.callMethod(testInstance, info.before.getName());
            ReflectionHelper.callMethod(testInstance, testMethod.getName());
            System.out.println("Test successfully finished: " + testMethod.getName());
            info.success++;
        } catch (Exception e) {
            System.out.println("Error during test execution: " + testMethod.getName());
            info.failed++;
        } finally {
            ReflectionHelper.callMethod(testInstance, info.after.getName());
        }
    }

    private TestInfo parseTestClass(Class<?> clazz) {
        var info = new TestInfo();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                info.tests.add(method);
            } else if (method.isAnnotationPresent(Before.class)) {
                info.before = method;
            } else if (method.isAnnotationPresent(After.class)) {
                info.after = method;
            }
        }
        return info;
    }

    private class TestInfo {
        private Method before;
        private Method after;
        private final ArrayList<Method> tests = new ArrayList<>();
        private Integer success = 0;
        private Integer failed = 0;

        @Override
        public String toString() {
            return String.format("Results: Total[%s], Success[%s], Failed[%s]%n", tests.size(), success, failed);
        }
    }
}
