package io.github.splotycode.mosaik.annotationbase.data;

import io.github.splotycode.mosaik.annotations.*;
import io.github.splotycode.mosaik.annotations.visibility.VisibilityLevel;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Getter
public class DataEntity {

    private String serviceName;
    private Class serviceClass;

    private Class<?> clazz;

    private String name;
    private HashMap<String, DataProperty> properties = new HashMap<>();
    private ArrayList<Entity> entityTags = new ArrayList<>();

    private VisibilityLevel fieldVisibility;
    @Setter private PropertyAnnotationHandle propertyAnnotationHandle;
    private Predicate<AnnotatedElement> propertyVisibilityCondition;

    public DataEntity(String serviceName, Class serviceClass, Object object, VisibilityLevel fieldVisibility) {
        this.serviceName = serviceName;
        this.serviceClass = serviceClass;
        this.fieldVisibility = fieldVisibility;
        clazz = object.getClass();

        investigateEntityTags();
        name = computeName();

        generateVisibilityConditions();

        scanMethods();
        scanFields();
    }

    private void generateVisibilityConditions() {
        propertyVisibilityCondition = element ->
                element.getAnnotationsByType(Property.class).length != 0 ||
                (propertyAnnotationHandle != null && propertyAnnotationHandle.getAnnotation(element) != null);
    }

    private void investigateEntityTags() {
        for (Entity entity : clazz.getAnnotationsByType(Entity.class)) {
            String displayName = "Entity '" + entity.value() + "'";
            if (isForThisService(displayName,
                    entity.services(), entity.servicesClasses(),
                    entity.disabledServices(), entity.disabledServicesClasses())) {
                entityTags.add(entity);
            }
        }
    }

    private boolean isForThisService(String displayName, String[] services, Class[] servicesClasses, String[] disabledServices, Class[] disabledServicesClasses) {
        boolean whitelist = services.length != 0 || servicesClasses.length != 0;
        boolean blacklist = disabledServices.length != 0 || disabledServicesClasses.length != 0;
        if (whitelist && blacklist) {
            throw new IllegalConfigurationException(displayName + " in " + clazz.getSimpleName() + " has a blacklist and whitelist");
        }

        if (whitelist) {
            return ArrayUtil.contains(services, serviceName) || ArrayUtil.contains(servicesClasses, serviceClass);
        } else if (blacklist) {
            return ArrayUtil.contains(disabledServices, serviceName) || ArrayUtil.contains(disabledServicesClasses, serviceClass);
        }
        return true;
    }

    private String computeName() {
        for (Entity entity : entityTags) {
            String name = entity.value();
            if (!name.isEmpty()) {
                return name;
            }
        }
        return clazz.getSimpleName();
    }

    private void iterateThroughClasses(BiConsumer<Class, Entity> consumer) {
        Class currentClass = clazz;
        Entity currentEntity;
        while (currentClass != null && ((currentEntity = (Entity) currentClass.getAnnotation(Entity.class)) == null || !currentEntity.ignoreSuper())) {
            consumer.accept(currentClass, currentEntity);
            currentClass = currentClass.getSuperclass();
        }
    }

    private void scanMethods() {
        iterateThroughClasses((clazz, entity) -> {
            for (Method method : clazz.getDeclaredMethods()) {
                HandleAsField annotation = method.getAnnotation(HandleAsField.class);
                if (annotation != null && AnnotationHelper.isVisible(fieldVisibility, method)) {
                    if (method.getParameterCount() != 0) {
                        throw new IllegalStateException("Methods with @HandleAsField may not have parameters");
                    }
                    String name = annotation.name();
                    if (name.isEmpty()) {
                        name = method.getName();
                    }
                    properties.put(name, DataProperty.fromMethod(this, method));
                }
            }
        });
    }

    private String getFieldName(Property property, Field field) {
        String name;
        if (property == null || (name = property.value()).isEmpty()) {
            if (propertyAnnotationHandle == null || (name = propertyAnnotationHandle.getName(field)).isEmpty()) {
                name = field.getName();
            }
        }
        return name;
    }

    private void scanFields() {
        iterateThroughClasses((clazz, entity) -> {
            for (Field field : clazz.getDeclaredFields()) {
                if (AnnotationHelper.isVisible(fieldVisibility, field, propertyVisibilityCondition)) {
                    Property property = getFieldProperty(field);
                    String name = getFieldName(property, field);
                    properties.putIfAbsent(name, DataProperty.fromField(this, field, property));
                }
            }
        });
    }

    private Property getFieldProperty(Field field) {
        for (Property property : field.getAnnotationsByType(Property.class)) {
            if (isPropertyValid(property)) {
                return property;
            }
        }
        return null;
    }

    private boolean isPropertyValid(Property property) {
        String displayName = "Property '" + property.value() + "'";
        return isForThisService(displayName,
                property.services(), property.servicesClasses(),
                property.disabledServices(), property.disabledServicesClasses());
    }

}
