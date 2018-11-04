package me.david.davidlib;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.david.davidlib.application.Application;
import me.david.davidlib.application.BootContext;
import me.david.davidlib.application.BootException;
import me.david.davidlib.utils.ClassFinderHelper;

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
            if (Application.class.isAssignableFrom(clazz)) {
                try {
                    applications.add((Application) clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new BootException("Could not create Instance of " + clazz.getSimpleName(), ex);
                }
            }
        }
        applications.forEach(application -> application.start(bootData));
    }
}
