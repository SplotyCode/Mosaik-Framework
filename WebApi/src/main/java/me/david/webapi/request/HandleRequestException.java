package me.david.webapi.request;

public class HandleRequestException extends RuntimeException {

    public HandleRequestException() {
    }

    public HandleRequestException(String s) {
        super(s);
    }

    public HandleRequestException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HandleRequestException(Throwable throwable) {
        super(throwable);
    }

    public HandleRequestException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
