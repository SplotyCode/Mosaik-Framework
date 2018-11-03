package me.david.webapi.handler.anotation;

import com.google.common.reflect.ClassPath;
import lombok.Getter;
import me.david.webapi.WebApplication;
import me.david.webapi.handler.HandlerFinder;
import me.david.webapi.handler.HttpHandler;
import me.david.webapi.handler.anotation.check.Handler;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnnotationHandlerFinder implements HandlerFinder {

    @Getter private static Class<Annotation>[] handlerAnotation = new Class[]{Handler.class};

    @Override
    public Collection<HttpHandler> search() {
        List<HttpHandler> handlers = new ArrayList<>();
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(WebApplication.getInstance().getClass().getClassLoader()).getAllClasses()) {
                String packageName = classInfo.getPackageName();
                if (packageName.startsWith("org.eclipse") ||
                        packageName.startsWith("javax") ||
                        packageName.startsWith("javafx") ||
                        packageName.startsWith("sun") ||
                        packageName.startsWith("org.jboss") ||
                        packageName.startsWith("java") ||
                        packageName.startsWith("com.google") ||
                        packageName.startsWith("io.netty") ||
                        packageName.startsWith("lombok") ||
                        packageName.startsWith("org.apache") ||
                        packageName.startsWith("com.sun") ||
                        packageName.startsWith("com.oracle") ||
                        packageName.startsWith("org.checkerframework") ||
                        packageName.startsWith("jdk") ||
                        packageName.startsWith("org.ietf") ||
                        packageName.startsWith("org.omg") ||
                        packageName.startsWith("org.w3c") ||
                        packageName.startsWith("com.intellij") ||
                        packageName.startsWith("org.jcp") ||
                        packageName.startsWith("org.xml") ||
                        packageName.startsWith("org.classpath") ||
                        packageName.startsWith("org.GNOME") ||
                        packageName.startsWith("netscape.javascript") ||
                        packageName.startsWith("me.david.webapi") ||
                        packageName.startsWith("org.codehaus")) {
                    continue;
                }
                System.out.println("Inspecting: " + classInfo.getName());
                Class<?> clazz = classInfo.load();
                for (Class<Annotation> annotation : handlerAnotation) {
                    if (clazz.isAnnotationPresent(annotation)) {
                        add(clazz, handlers);
                        break;
                    }
                    boolean cancel = false;
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(annotation)) {
                            add(clazz, handlers);
                            cancel = true;
                            break;
                        }
                    }
                    if (cancel) break;
                }
            }
        } catch (IOException | IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return handlers;
    }

    private void add(Class<?> clazz, List<HttpHandler> handlers) throws IllegalAccessException, InstantiationException {
        System.out.println("Added " + clazz.getSimpleName());
        Object obj = clazz.newInstance();
        AnnotationHandler handler = new AnnotationHandler(obj);
        handlers.add(handler);
    }

}
