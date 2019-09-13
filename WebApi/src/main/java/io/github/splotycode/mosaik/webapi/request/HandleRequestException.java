package io.github.splotycode.mosaik.webapi.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HandleRequestException extends RuntimeException {

    private int errorCode;

    public HandleRequestException() {}

    public HandleRequestException(int errorCode) {
        this.errorCode = errorCode;
    }

    public HandleRequestException(String s) {
        super(s);
    }

    public HandleRequestException(String s, int errorCode) {
        super(s);
        this.errorCode = errorCode;
    }

    public HandleRequestException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HandleRequestException(String s, Throwable throwable, int errorCode) {
        super(s, throwable);
        this.errorCode = errorCode;
    }

    public HandleRequestException(Throwable throwable) {
        super(throwable);
    }

    public HandleRequestException(Throwable throwable, int errorCode) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public HandleRequestException(String s, Throwable throwable,
                                  boolean enableSuppression, boolean writableStackTrace) {
        super(s, throwable, enableSuppression, writableStackTrace);
    }
}
