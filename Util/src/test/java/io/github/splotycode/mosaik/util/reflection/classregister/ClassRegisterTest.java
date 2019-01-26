package io.github.splotycode.mosaik.util.reflection.classregister;

import io.github.splotycode.mosaik.util.reflection.ClassCollector;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ClassRegisterTest {

    @Test
    @Disabled
    public void testRegisterAll() {
        ClassCollector collector = ClassCollector.newInstance()
                .setNoDisableds(true)
                .setInPackage("io.github.splotycode.mosaik.util.reflection")
                .setNeedAssignable(Object.class);
        ListClassRegister<Object> register = new ListClassRegister<>(new ArrayList<>(), Object.class);
        register.registerAll(collector);
        System.out.println(register.getList().size());
    }

}
