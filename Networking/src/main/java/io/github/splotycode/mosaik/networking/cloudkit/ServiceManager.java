package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.util.collection.MultiHashMap;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServiceManager {

    private final CloudKit cloudKit;

    public ServiceManager(CloudKit cloudKit) {
        this.cloudKit = cloudKit;
    }

    private final HashMap<String, Service> services = new HashMap<>();

    @Getter private final Collection<Service> serviceCollection = new CopyOnWriteArrayList<>();

    private final HashMap<Class, Service> classMap = new HashMap<>();
    private final MultiHashMap<String, Class> mappedClasses = new MultiHashMap<>();

    void stop(Service service) {
        String name = service.displayName();
        service.stop();
        synchronized (this) {
            services.remove(name);
            mappedClasses.remove(name).forEach(classMap::remove);
        }
    }


    void start(Service service, Runnable beforeInitialize) {
        String name = service.displayName();
        Class<? extends Service> clazz = service.getClass();

        synchronized (this) {
            Service old = services.put(name, service);
            if (old != null) {
                throw new ServiceException("Service with name " + name + " already started");
            }
            registerServiceClass(service, name, clazz);
        }
        serviceCollection.add(service);

        beforeInitialize.run();
        if (service instanceof CloudKitService) {
            ((CloudKitService) service).initialize(cloudKit);
        }
        service.start();
    }

    private void registerServiceClass(Service service, String name, Class<? extends Service> clazz) {
        boolean isSingleton = service.singleton();

        Service clash = classMap.putIfAbsent(clazz, service);
        if (clash != null) {
            String clashName = clash.displayName();

            if (clash.singleton() && clazz.isInstance(clash)) {
                services.remove(name);
                if (isSingleton) {
                    throw new ServiceException(clazz.getSimpleName() + " is a singleton but was two instances " + clashName + " and " + name);
                } else {
                    throw new ServiceException("Same ServiceClass(" + clazz.getSimpleName() + ") reports it is a singleton in " + clashName + " but is not in " + name);
                }
            } else {
                /* Override */
                classMap.put(clazz, service);
                mappedClasses.get(clashName).remove(clazz);
            }
        }
        mappedClasses.addToList(name, clazz);
        Class superClass = clazz;
        int deapth = 0;
        while (superClass != null) {
            for (Class inter : superClass.getInterfaces()) {
                if (Service.class != inter) {
                    checkSuperClass(service, name, isSingleton, inter, deapth + 1);
                }
            }
            if (deapth != 0) {
                checkSuperClass(service, name, isSingleton, superClass, deapth);
            }
            deapth++;
            superClass = superClass.getSuperclass();
        }
    }

    private int calculateDepth(Class base, Class subClass) {
        boolean isInterface = subClass.isInterface();
        int deapth = 0;
        while (base != null) {
            if (isInterface) {
                for (Class inter : base.getInterfaces()) {
                    if (subClass == inter) {
                        return deapth + 1;
                    }
                }
            } else if (base == subClass) {
                return deapth;
            }
            deapth++;
            base = subClass.getSuperclass();
        }
        throw new IllegalStateException("Failed to calculate depth: Could not find " + (isInterface ? "Interface" : "Superclass"));
    }

    private void checkSuperClass(Service service, String name, boolean isSingleton, Class clazz, int depth) {
        Service clash = classMap.putIfAbsent(clazz, service);
        if (clash != null) {
            if (clazz.isInstance(clash)) {
                /* Primary always wins against non primary */
                return;
            }

            boolean clashSingleton = clash.singleton();

            if (clashSingleton && !isSingleton) {
                return;
            }

            if (clashSingleton == isSingleton) {
                if (depth <= calculateDepth(clash.getClass(), clazz)) {
                    return;
                }
            }

            /* We can safely override now */
            classMap.put(clazz, service);
            mappedClasses.get(clash.displayName()).remove(clazz);
        }
        mappedClasses.addToList(name, clazz);
    }

    @SuppressWarnings("unchecked")
    public synchronized <S extends Service> S getServiceByClass(Class<S> clazz) {
        return (S) classMap.get(Objects.requireNonNull(clazz, "clazz"));
    }

    public <S extends Service> S requireServiceByClass(Class<S> clazz) {
        S service = getServiceByClass(clazz);
        if (service == null) {
            throw new ServiceNotAvailableException("Could not find service by class: " + clazz.getSimpleName());
        }
        return service;
    }

    public synchronized Service getServiceByName(String name) {
        return services.get(Objects.requireNonNull(name, "name"));
    }

    public Service requireServiceByName(String name) {
        Service service = getServiceByName(name);
        if (service == null) {
            throw new ServiceNotAvailableException("Could not find service by name: " + name);
        }
        return service;
    }

    private static class ServiceException extends RuntimeException {

        public ServiceException() {
        }

        public ServiceException(String message) {
            super(message);
        }

        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }

        public ServiceException(Throwable cause) {
            super(cause);
        }

        public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

    }
}
