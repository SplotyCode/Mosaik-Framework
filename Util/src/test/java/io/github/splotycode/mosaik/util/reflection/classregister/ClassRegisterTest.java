package io.github.splotycode.mosaik.util.reflection.classregister;

import io.github.splotycode.mosaik.util.reflection.classpath.ClassPath;
import io.github.splotycode.mosaik.util.reflection.collector.ClassCollector;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ClassRegisterTest {

    @Test
    @Disabled
    public void testRegisterAll() {
        ClassCollector collector = ClassCollector.newInstance()
                .setNoDisable(true)
                .setInPackage("io.github.splotycode.mosaik.util.reflection")
                .setNeedAssignable(Object.class);
        ListClassRegister<Object> register = new ListClassRegister<>(new ArrayList<>(), Object.class);
        register.registerAll(collector, ClassPath.fromClassLoader(ClassRegisterTest.class.getClassLoader()));
        System.out.println(register.getList().size());
    }

}
