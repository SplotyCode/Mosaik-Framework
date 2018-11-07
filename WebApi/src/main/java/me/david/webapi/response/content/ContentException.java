package me.david.webapi.response.content;

public class ContentException extends RuntimeException {

    public ContentException() {
    }

    public ContentException(String s) {
        super(s);
    }

    public ContentException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ContentException(Throwable throwable) {
        super(throwable);
    }

    public ContentException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
