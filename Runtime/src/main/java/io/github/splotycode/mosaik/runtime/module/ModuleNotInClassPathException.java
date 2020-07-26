package io.github.splotycode.mosaik.runtime.module;

public class ModuleNotInClassPathException extends RuntimeException {

    public ModuleNotInClassPathException() {
    }

    public ModuleNotInClassPathException(String s) {
        super(s);
    }

    public ModuleNotInClassPathException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModuleNotInClassPathException(Throwable throwable) {
        super(throwable);
    }

    public ModuleNotInClassPathException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
