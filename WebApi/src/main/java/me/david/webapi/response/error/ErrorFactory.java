package me.david.webapi.response.error;

import me.david.webapi.response.Response;

public interface ErrorFactory {

    Response generatePage(Throwable throwable);

    boolean valid(Throwable throwable);

}
