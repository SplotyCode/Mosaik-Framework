package io.github.splotycode.mosaik.webapi.response.error;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.response.content.string.StaticStringContent;

public class DefaultErrorFactory implements ErrorFactory {

    @Override
    public Response generatePage(Throwable throwable) {
        Response response = new Response(new StaticStringContent("<h1>Internal Server Error</h1> <code><pre>" + ExceptionUtil.toString(throwable) + "</pre></code>"));
        while (throwable != null) {
            if (throwable instanceof HandleRequestException) {
                int errorCode = ((HandleRequestException) throwable).getErrorCode();
                if (errorCode != 0) {
                    response.setResponseCode(errorCode);
                    return response;
                }
            }
            throwable = throwable.getCause();
        }
        response.setResponseCode(500);
        return response;
    }

    @Override
    public boolean valid(Throwable throwable) {
        return true;
    }

}
