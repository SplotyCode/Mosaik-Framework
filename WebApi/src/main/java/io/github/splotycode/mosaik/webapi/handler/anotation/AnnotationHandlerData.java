package io.github.splotycode.mosaik.webapi.handler.anotation;

import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.reflection.annotation.data.AnnotationData;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IMethodData;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.webapi.handler.UrlPattern;
import io.github.splotycode.mosaik.webapi.handler.anotation.check.*;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.cache.Cache;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.cache.CacheDefaultProvider;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.request.RequestHeader;
import io.github.splotycode.mosaik.webapi.response.ContentType;
import io.github.splotycode.mosaik.webapi.response.HttpCashingConfiguration;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class AnnotationHandlerData extends AnnotationData {

    private UrlPattern mapping = null;
    private String httpMethod = null;
    private boolean costomMethod = false;
    private String host;
    private List<String> neededGet = new ArrayList<>(), neededPost = new ArrayList<>();
    private HashMap<String, String> getMustBe = new HashMap<>(), postMustBe = new HashMap<>();
    private HttpCashingConfiguration cashingConfiguration;
    private ContentType contentType;

    @Override
    public void buildData(Annotation[] annotations) {
        super.buildData(annotations);
        for (Annotation annotation : annotations) {
            if (annotation instanceof Mapping) {
                mapping = new UrlPattern(((Mapping) annotation).value());
            } else if (annotation instanceof NeedGetMethod) {
                setHttpMethod("GET");
            } else if (annotation instanceof NeedPostMethod) {
                setHttpMethod("POST");
            } else if (annotation instanceof NeedMethod) {
                setHttpMethod(((NeedMethod) annotation).method().toUpperCase());
                costomMethod = true;
            } else if (annotation instanceof NeedGetParameter) {
                setHttpMethod("GET");
                Collections.addAll(neededGet, ((NeedGetParameter) annotation).parameters());
            } else if (annotation instanceof NeedPostParameter) {
                setHttpMethod("POST");
                Collections.addAll(neededPost, ((NeedPostParameter) annotation).parameters());
            } else if (annotation instanceof GetMustBe) {
                setHttpMethod("GET");
                GetMustBe mustBeAnnotation = (GetMustBe) annotation;
                getMustBe.put(mustBeAnnotation.parameter(), mustBeAnnotation.value());
            } else if (annotation instanceof PostMustBe) {
                setHttpMethod("POST");
                PostMustBe mustBeAnnotation = (PostMustBe) annotation;
                postMustBe.put(mustBeAnnotation.parameter(), mustBeAnnotation.value());
            } else if (annotation instanceof Host) {
                host = (((Host) annotation).value()).trim().toLowerCase(Locale.ENGLISH);
            } else if (annotation instanceof CacheDefaultProvider) {
                cashingConfiguration = ((CacheDefaultProvider) annotation).value().get();
            } else if (annotation instanceof Cache) {
                cashingConfiguration = HttpCashingConfiguration.fromCache((Cache) annotation);
            } else if (annotation instanceof ResponseContentType) {
                contentType = ((ResponseContentType) annotation).value();
            }
        }
    }

    private void setHttpMethod(String method) {
        if (this.httpMethod != null && !method.equals(this.httpMethod))
            throw new IllegalStateException("Can not force two different methods (" + method + " and " + this.httpMethod);
        this.httpMethod = method;
    }

    void applyContentType(Response response) {
        if (contentType != null) {
            response.setContentType(contentType);
        }
    }

    void applyCashingConfiguration(Response response) {
        try {
            if (cashingConfiguration != null) {
                response.applyCashingConfiguration(cashingConfiguration);
            }
        } catch (Throwable throwable) {
            throw new HandleRequestException("Failed to apply cashing configuration");
        }
    }

    public boolean valid(Request request) {
        String host = request.getHeader(RequestHeader.HOST);
        if (host == null) {
            if (this.host != null) return false;
        } else if (!host.trim().toLowerCase(Locale.ENGLISH).equals(host)) {
             return false;
        }
        if (mapping != null && !mapping.match(request.getPath()).isMatch()) return false;
        if (httpMethod != null && (costomMethod ? request.getMethod().getMethod().matches(httpMethod) : request.getMethod().getMethod().equals(httpMethod))) return false;

        for (String get : neededGet)
            if (!request.getGet().containsKey(get))
                return false;

        for (String get : neededPost)
            if (!request.getPost().containsKey(get))
                return false;

        for (Map.Entry<String, String> pair : getMustBe.entrySet()) {
            if (!request.getGet().containsKey(pair.getKey()) || !request.getFirstGetParameter(pair.getKey()).equals(pair.getValue()))
                return false;
        }

        for (Map.Entry<String, String> pair : postMustBe.entrySet()) {
            if (!request.getPost().containsKey(pair.getKey()) || !request.getFirstPostParameter(pair.getKey()).equals(pair.getValue()))
                return false;
        }
        return true;
    }

    @Getter
    public static class SupAnnotationHandlerData extends AnnotationHandlerData implements IMethodData {

        private List<Pair<ParameterResolver, Parameter>> parameters = new ArrayList<>();
        private boolean returnContext;

        @Override
        public void buildData(Annotation[] annotations) {
            super.buildData(annotations);
            Class<?> returnType = getMethod().getReturnType();
            returnContext = ResponseContent.class.isAssignableFrom(returnType);
            if (!returnContext && returnType != boolean.class &&
                    returnType != Boolean.class &&
                    returnType != void.class) {
                throw new IllegalHandlerException("Invalid httpMethod type of handler " + displayName);
            }
        }

        @Override
        public void registerParameter(Parameter parameter, ParameterResolver parameterResolver) {
            parameters.add(new Pair<>(parameterResolver, parameter));
        }

        @Override
        public Collection<Pair<ParameterResolver, Parameter>> getAllParameters() {
            return parameters;
        }
    }

}
