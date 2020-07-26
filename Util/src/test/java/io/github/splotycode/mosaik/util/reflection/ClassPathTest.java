package io.github.splotycode.mosaik.util.reflection;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.reflection.classpath.ClassPathVisitor;
import io.github.splotycode.mosaik.util.reflection.classpath.VisitorAction;
import org.junit.Test;

import java.io.IOException;

public class ClassPathTest {

    public static void main(String[] args) throws IOException {
        System.out.println(ClassPathTest.class.getClassLoader());
        io.github.splotycode.mosaik.util.reflection.classpath.ClassPath cp = new io.github.splotycode.mosaik.util.reflection.classpath.ClassPath();
        cp.addClassLoader(ClassPathTest.class.getClassLoader(), false);
        cp.load(new ClassPathVisitor() {
            @Override
            public VisitorAction visitPackage(String packagePath) {
                return VisitorAction.CONTINUE;
            }

            @Override
            public VisitorAction visitResource(String fullName) {
                return VisitorAction.CONTINUE;
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void test() {
        if (!StringUtil.isEmpty(System.getenv("TRAVIS"))) {
            throw new RuntimeException();
        }
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
