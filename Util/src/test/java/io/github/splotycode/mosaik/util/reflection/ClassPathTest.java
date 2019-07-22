package io.github.splotycode.mosaik.util.reflection;

import org.junit.Test;

public class ClassPathTest {

    @Test(expected = RuntimeException.class)
    public void test() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassPath path = new ClassPath(getClass().getClassLoader());
        if (classLoader != null) {
            path.classes(s -> {
                throw new RuntimeException();
            });
        }
        classLoader = ClassPathTest.class.getClassLoader();
        path = new ClassPath(classLoader);
        path.classes(s -> {
            throw new RuntimeException();
        });
    }
}
