package io.github.splotycode.mosaik.util.reflection.modules;

public class ModuleNotInClassPathExcpetion extends RuntimeException {

    public ModuleNotInClassPathExcpetion() {
    }

    public ModuleNotInClassPathExcpetion(String s) {
        super(s);
    }

    public ModuleNotInClassPathExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModuleNotInClassPathExcpetion(Throwable throwable) {
        super(throwable);
    }

    public ModuleNotInClassPathExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
