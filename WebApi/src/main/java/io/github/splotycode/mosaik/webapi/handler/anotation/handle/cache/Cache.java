package io.github.splotycode.mosaik.webapi.handler.anotation.handle.cache;

import io.github.splotycode.mosaik.webapi.response.HttpCashingConfiguration;

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

    HttpCashingConfiguration.ETagMode eTagMode() default HttpCashingConfiguration.ETagMode.SHA_512;

    HttpCashingConfiguration.ValidationMode[] modes();

}
