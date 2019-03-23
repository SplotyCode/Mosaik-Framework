package io.github.splotycode.mosaik.spigotlib.command.command;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand implements ICommand {

    private List<String> aliases;
    private String usage, description;

    public AbstractCommand(String usage, String description, String... aliases) {
        this.aliases = Arrays.asList(aliases);
        this.usage = usage;
        this.description = description;
    }

    public AbstractCommand(String usage, String description, List<String> aliases) {
        this.aliases = aliases;
        this.usage = usage;
        this.description = description;
    }

    @Override
    public List<String> aliases() {
        return aliases;
    }

    @Override
    public String desciption() {
        return description;
    }

    @Override
    public String usage() {
        return usage;
    }

}
