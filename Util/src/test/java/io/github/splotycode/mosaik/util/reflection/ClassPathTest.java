package io.github.splotycode.mosaik.util.reflection;

import org.junit.Test;

public class ClassPathTest {

    @Test(expected = RuntimeException.class)
    public void test() {
        ClassPath path = new ClassPath(getClass().getClassLoader());
        path.resources(s -> {
            throw new RuntimeException();
        });
    }
}
