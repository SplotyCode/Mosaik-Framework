package io.github.splotycode.mosaik.spigotlib.command.annotation;

import io.github.splotycode.mosaik.spigotlib.SpigotApplicationType;
import io.github.splotycode.mosaik.spigotlib.command.annotation.annotations.Command;
import io.github.splotycode.mosaik.util.reflection.classregister.ClassRegister;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;
import java.util.Collection;

@AllArgsConstructor
public class AnnotationRegistration implements ClassRegister {

    private SpigotApplicationType application;

    @Override
    public void register(Object obj) {
        Class<?> clazz = obj.getClass();
        if (clazz.isAnnotationPresent(Command.class)) {
            String basePath = clazz.getAnnotation(Command.class).value();
            for (Method method : clazz.getDeclaredMethods()) {
                Command command = method.getAnnotation(Command.class);
                if (command != null) {
                    String path = basePath + " " + command.value();
                    AnnotationCommand annotationCommand = new AnnotationCommand(command, method, obj);
                    application.getCommandHead().group(path).register(annotationCommand);
                }
            }
        }
    }

    @Override
    public void unRegister(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class getObjectClass() {
        return Object.class;
    }
}
