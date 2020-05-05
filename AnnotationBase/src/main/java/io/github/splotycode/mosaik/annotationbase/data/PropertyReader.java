package io.github.splotycode.mosaik.annotationbase.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface PropertyReader {

    static PropertyReader fromMethod(Method method) {
        method.setAccessible(true);
        return entity -> {
            try {
                return method.invoke(entity);
            } catch (InvocationTargetException | ExceptionInInitializerError e) {
                throw new PropertyReadException("Error while calling the underlying method", e);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new PropertyReadException("Failed to call underlying method", e);
            }
        };
    }

    static PropertyReader fromField(Field field) {
        field.setAccessible(true);
        return entity -> {
            try {
                return field.get(entity);
            } catch (ExceptionInInitializerError e) {
                throw new PropertyReadException("Error initializing field", e);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new PropertyReadException("Failed to get data from field", e);
            }
        };
    }

    class PropertyReadException extends RuntimeException {

        public PropertyReadException(String message) {
            super(message);
        }

        public PropertyReadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    Object readValue(Object entity);

}
