package io.github.splotycode.mosaik.webapi.response.error;

import io.github.splotycode.mosaik.webapi.response.Response;

public interface ErrorFactory {

    Response generatePage(Throwable throwable);

    boolean valid(Throwable throwable);

}
