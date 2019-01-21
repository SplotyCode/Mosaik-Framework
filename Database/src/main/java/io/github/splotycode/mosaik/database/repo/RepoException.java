package io.github.splotycode.mosaik.database.repo;

/**
 * Throws when a Exception on executing a command on a repo accrued
 */
public class RepoException extends RuntimeException {

    public RepoException() {
    }

    public RepoException(String s) {
        super(s);
    }

    public RepoException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RepoException(Throwable throwable) {
        super(throwable);
    }

    public RepoException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
