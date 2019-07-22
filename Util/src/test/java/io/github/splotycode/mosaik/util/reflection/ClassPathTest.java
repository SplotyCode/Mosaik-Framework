package io.github.splotycode.mosaik.util.reflection;

import org.junit.Test;

public class ClassPathTest {

    @Test(expected = RuntimeException.class)
    public void test() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            test(new ClassPath(classLoader));
        }
        test(new ClassPath(ClassPathTest.class.getClassLoader()));
    }

    private void test(ClassPath path) {
        path.classes(s -> {
            throw new RuntimeException();
        });
        path.resources(s -> {
            throw new RuntimeException();
        });
    }
}
