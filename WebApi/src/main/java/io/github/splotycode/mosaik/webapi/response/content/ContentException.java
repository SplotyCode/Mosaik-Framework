package io.github.splotycode.mosaik.webapi.response.content;

import io.github.splotycode.mosaik.webapi.request.HandleRequestException;

public class ContentException extends HandleRequestException {

    public ContentException() {
    }

    public ContentException(int errorCode) {
        super(errorCode);
    }

    public ContentException(String s) {
        super(s);
    }

    public ContentException(String s, int errorCode) {
        super(s, errorCode);
    }

    public ContentException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ContentException(String s, Throwable throwable, int errorCode) {
        super(s, throwable, errorCode);
    }

    public ContentException(Throwable throwable) {
        super(throwable);
    }

    public ContentException(Throwable throwable, int errorCode) {
        super(throwable, errorCode);
    }

    public ContentException(String s, Throwable throwable,
                            boolean enableSuppression, boolean writableStackTrace) {
        super(s, throwable, enableSuppression, writableStackTrace);
    }
}
