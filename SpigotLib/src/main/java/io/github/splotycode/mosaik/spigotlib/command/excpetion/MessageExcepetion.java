package io.github.splotycode.mosaik.spigotlib.command.excpetion;

import io.github.splotycode.mosaik.spigotlib.SpigotApplicationType;

public class MessageExcepetion extends CommandExcpetion {

    private String[] args;

    public MessageExcepetion() {
    }

    public MessageExcepetion(String s) {
        super(s);
    }

    public MessageExcepetion(String s, String... args) {
        super(s);
        this.args = args;
    }

    public String getMessage(SpigotApplicationType application) {
        return application.getI18N().get(getMessage(), args);
    }
}
