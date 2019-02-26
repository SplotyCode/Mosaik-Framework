package io.github.splotycode.mosaik.webapi.response;

import java.util.Date;

//TODO
public class HttpCashingConfiguration {

    private long expires = -1;

    public void apply(Response response) {
        if (expires != -1) {
            Date date = new Date(expires * 1000L + System.currentTimeMillis());
            response.setHeader(ResponseHeader.EXPIRES, Response.DATE_FORMAT.format(date));
        }
    }

    public enum ValidationMode {

        ETAG,
        MODIFIED

    }

    public enum ETagMode {

        SHA_512,
        MD5

    }

}
