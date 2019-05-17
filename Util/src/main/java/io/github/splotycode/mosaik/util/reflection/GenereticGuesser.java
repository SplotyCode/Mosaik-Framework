package io.github.splotycode.mosaik.util.reflection;

import io.github.splotycode.mosaik.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class GenereticGuesser {

    @Getter @Setter
    public static GenereticGuesser instance = new GenereticGuesser();

    private static Map<Pair<Object, String>, Class<?>> cache = new HashMap<>();

    public static Class<?> find(Object object, Class<?> superclass, String typeParamName) {
        Pair<Object, String> key = new Pair<>(object, (superclass != null ? superclass.getName() + "##" : "")  + typeParamName);
        Class<?> clazz = cache.get(key);
        if (clazz == null) {
            clazz = getInstance().gues(object, superclass, typeParamName);
            cache.put(key, clazz);
        }
        return clazz;
    }

    public Class<?> gues(Object object, Class<?> superclass, String typeParamName) {
        Class<?> thisClass = object.getClass();
        Class currentClass = thisClass;

        do {
            while(currentClass.getSuperclass() != superclass) {
                currentClass = currentClass.getSuperclass();
                if (currentClass == null) {
                    throw new FailedFindSuperExcepeion("Failed find super class " + superclass.getName() + " in " + thisClass.getName());
                }
            }

            int typeParamIndex = -1;
            TypeVariable<?>[] typeParams = currentClass.getSuperclass().getTypeParameters();

            for(int i = 0; i < typeParams.length; ++i) {
                if (typeParamName.equals(typeParams[i].getName())) {
                    typeParamIndex = i;
                    break;
                }
            }

            if (typeParamIndex < 0) {
                throw new IllegalStateException("unknown type parameter '" + typeParamName + "': " + superclass);
            }

            Type genericSuperType = currentClass.getGenericSuperclass();
            if (!(genericSuperType instanceof ParameterizedType)) {
                return Object.class;
            }

            Type[] actualTypeParams = ((ParameterizedType)genericSuperType).getActualTypeArguments();
            Type actualTypeParam = actualTypeParams[typeParamIndex];
            if (actualTypeParam instanceof ParameterizedType) {
                actualTypeParam = ((ParameterizedType)actualTypeParam).getRawType();
            }

            if (actualTypeParam instanceof Class) {
                return (Class)actualTypeParam;
            }

            if (actualTypeParam instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType)actualTypeParam).getGenericComponentType();
                if (componentType instanceof ParameterizedType) {
                    componentType = ((ParameterizedType)componentType).getRawType();
                }

                if (componentType instanceof Class) {
                    return Array.newInstance((Class)componentType, 0).getClass();
                }
            }

            if (!(actualTypeParam instanceof TypeVariable)) {
                throw new IllegalArgumentException(typeParamName + " is not a TypeVariable");
            }

            TypeVariable<?> v = (TypeVariable)actualTypeParam;
            currentClass = thisClass;
            if (!(v.getGenericDeclaration() instanceof Class)) {
                return Object.class;
            }

            superclass = (Class)v.getGenericDeclaration();
            typeParamName = v.getName();
        } while(superclass.isAssignableFrom(thisClass));

        return Object.class;
    }

    public static class FailedFindSuperExcepeion extends RuntimeException {

        public FailedFindSuperExcepeion() {
        }

        public FailedFindSuperExcepeion(String s) {
            super(s);
        }

        public FailedFindSuperExcepeion(String s, Throwable throwable) {
            super(s, throwable);
        }

        public FailedFindSuperExcepeion(Throwable throwable) {
            super(throwable);
        }

        public FailedFindSuperExcepeion(String s, Throwable throwable, boolean b, boolean b1) {
            super(s, throwable, b, b1);
        }
    }

}
