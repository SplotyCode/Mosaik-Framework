package me.david.davidlib;

import lombok.Getter;
import me.david.davidlib.annotation.Disabled;
import me.david.davidlib.application.Application;
import me.david.davidlib.application.ApplicationType;
import me.david.davidlib.application.BootContext;
import me.david.davidlib.application.BootException;
import me.david.davidlib.utils.ClassFinderHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Main {

    @Getter private static Main instance = new Main();

    private List<Application> applications = new ArrayList<>();
    private static BootContext bootData;

    public static void main(String[] args) {
        bootData = new BootContext(args, System.currentTimeMillis());
    }

    private Main() {
        for (Class<?> clazz : ClassFinderHelper.getUserClasses()) {
            if (Application.class.isAssignableFrom(clazz) &&
                    !Modifier.isAbstract(clazz.getModifiers()) &&
                    clazz.isAnnotationPresent(Disabled.class)) {
                try {
                    Application application = (Application) clazz.newInstance();
                    applications.add(application);
                    for (Class<?> type : clazz.getInterfaces()) {
                        if (ApplicationType.class.isAssignableFrom(type)) {
                            application.getApplicationTypes().add((Class<ApplicationType>) type);
                        }
                    }
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new BootException("Could not create Instance of " + clazz.getSimpleName(), ex);
                }
            }
        }
        applications.forEach(application -> {
            application.getApplicationTypes().forEach(type -> {
                try {
                    application.getClass().getMethod("initType", BootContext.class, type).invoke(application, bootData, null);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new BootException("Could not init Application Type " + type.getClass().getSimpleName() + " in " + application.getName());
                }
            });
            application.start(bootData);
        });
    }
}
