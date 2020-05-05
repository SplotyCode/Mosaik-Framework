package io.github.splotycode.mosaik.annotationbase.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface PropertyReader {

    static PropertyReader fromMethod(Method method, Object object) {
        method.setAccessible(true);
        return () -> {
            try {
                return method.invoke(object);
            } catch (InvocationTargetException | ExceptionInInitializerError e) {
                throw new PropertyReadException("Error while calling the underlying method", e);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new PropertyReadException("Failed to call underlying method", e);
            }
        };
    }

    static PropertyReader fromField(Field field, Object object) {
        field.setAccessible(true);
        return () -> {
            try {
                return field.get(object);
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

    Object readValue();

}
