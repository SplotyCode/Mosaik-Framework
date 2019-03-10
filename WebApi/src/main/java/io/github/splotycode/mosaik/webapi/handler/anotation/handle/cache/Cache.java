package io.github.splotycode.mosaik.webapi.handler.anotation.handle.cache;

import io.github.splotycode.mosaik.webapi.response.HttpCashingConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Cache {

    long expires() default  -1;

    boolean noCache() default false;
    boolean noStore() default false;
    boolean noTransform() default false;
    boolean onlyIfCashed() default false;
    boolean mustRevalidate() default false;
    boolean isPublic() default false;
    boolean isPrivate() default false;

    long maxAge() default -1;
    long maxStale() default -1;
    long minFresh() default -1;

    HttpCashingConfiguration.DefaultETagMode eTagMode() default HttpCashingConfiguration.DefaultETagMode.SHA_512;

    HttpCashingConfiguration.ValidationMode[] modes();

}
