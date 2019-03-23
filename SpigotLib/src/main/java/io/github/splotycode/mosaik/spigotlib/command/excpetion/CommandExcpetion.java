package io.github.splotycode.mosaik.spigotlib.command.excpetion;

public class CommandExcpetion extends RuntimeException {

    public CommandExcpetion() {
    }

    public CommandExcpetion(String s) {
        super(s);
    }

    public CommandExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CommandExcpetion(Throwable throwable) {
        super(throwable);
    }

    public CommandExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
