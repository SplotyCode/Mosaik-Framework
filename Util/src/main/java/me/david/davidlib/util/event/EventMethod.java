package me.david.davidlib.util.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

@AllArgsConstructor
public class EventMethod {

    @Getter private final Method method;
    @Getter private final Object source;

}
