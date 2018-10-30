package me.david.webapi.handler.anotation;

import lombok.*;
import me.david.webapi.handler.anotation.check.*;
import me.david.webapi.server.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@EqualsAndHashCode
@Getter
@Setter
public class AnnotationHandlerData {

    private String mapping = null;
    private int priority;
    private String method = null;
    private boolean costomMethod = false;

    public AnnotationHandlerData(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Mapping) {
                mapping = ((Mapping) annotation).path();
            } else if (annotation instanceof Priority) {
                priority = ((Priority) annotation).priority();
            } else if (annotation instanceof Last) {
                priority = Integer.MIN_VALUE;
            } else if (annotation instanceof First) {
                priority = Integer.MAX_VALUE;
            } else if (annotation instanceof NeedGetMethod) {
                method = "GET";
            } else if (annotation instanceof NeedPostMethod) {
                method = "POST";
            } else if (annotation instanceof NeedMethod) {
                method = ((NeedMethod) annotation).method().toUpperCase();
                costomMethod = true;
            }
        }
    }

    public boolean valid(Request request) {
        if (mapping != null && request.getPath().matches(mapping)) return false;
        if (method != null && (costomMethod ? request.getMethod().getMethod().matches(method) : request.getMethod().getMethod().equals(method))) return false;
        return true;
    }

    @Getter
    public static class SupAnnotationHandlerData extends AnnotationHandlerData {

        private Method method;

        public SupAnnotationHandlerData(Annotation[] annotations, Method method) {
            super(annotations);
            this.method = method;
        }
    }

}
