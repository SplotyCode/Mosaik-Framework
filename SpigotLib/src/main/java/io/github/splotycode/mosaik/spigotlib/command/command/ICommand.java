package io.github.splotycode.mosaik.spigotlib.command.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ICommand {

    void call(CommandSender sender, String[] args) throws Exception;

    List<String> aliases();

    String desciption();
    String usage();

}
