package me.david.davidlib.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventManager {

    private HashMap<Class<? extends Event>, HashMap<Integer, ArrayList<EventMethod>>> listeners = new HashMap<>();

    public void register(final Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.getParameterTypes().length == 1 && method.isAnnotationPresent(EventTarget.class)) {
                final Class<? extends Event> clazz = (Class<? extends Event>) method.getParameterTypes()[0];
                final EventMethod eventmethod = new EventMethod(method, obj);
                final EventTarget anotation = method.getAnnotation(EventTarget.class);

                if (listeners.containsKey(clazz)) {
                    HashMap<Integer, ArrayList<EventMethod>> data = listeners.get(clazz);
                    if (data.containsKey(anotation.priority())) {
                        ArrayList<EventMethod> list = data.get(anotation.priority());
                        list.add(eventmethod);
                    } else data.put(anotation.priority(), new ArrayList<EventMethod>(){{
                        add(eventmethod);
                    }});
                } else listeners.put(clazz, new HashMap<Integer, ArrayList<EventMethod>>(){{
                    put(anotation.priority(), new ArrayList<EventMethod>(){{
                        add(eventmethod);
                    }});
                }});
            }
        }
    }

    public void unRegister(final Object obj) {
        for (HashMap<Integer, ArrayList<EventMethod>> methods : listeners.values())
            for (ArrayList<EventMethod> methodData : methods.values())
                for (EventMethod method : new ArrayList<>(methodData))
                    if (method.getSource().equals(obj)) {
                        methodData.remove(method);
                        return;
                    }
    }

    public void call(Event event) {
        String name = event.getClass().getSimpleName();
        for (Map.Entry<Class<? extends Event>, HashMap<Integer, ArrayList<EventMethod>>> method : listeners.entrySet()) {
            if (method.getKey().getSimpleName().equals(name)) {
                HashMap<Integer, ArrayList<EventMethod>> listeners = method.getValue();
                listeners.keySet().stream().sorted(Integer::compare).forEach(prio -> {
                    for (EventMethod listener : listeners.get(prio)) {
                        try {
                            listener.getMethod().invoke(listener.getSource(), event);
                        } catch (ReflectiveOperationException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                break;
            }
        }
    }
}
