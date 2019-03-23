package io.github.splotycode.mosaik.spigotlib.command;

import io.github.splotycode.mosaik.spigotlib.SpigotApplicationType;
import io.github.splotycode.mosaik.spigotlib.command.command.ICommand;
import io.github.splotycode.mosaik.spigotlib.command.excpetion.CommandExcpetion;
import io.github.splotycode.mosaik.spigotlib.command.excpetion.MessageExcepetion;
import io.github.splotycode.mosaik.spigotlib.permission.Permissions;
import io.github.splotycode.mosaik.util.ExceptionUtil;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CommandRedirect implements CommandExecutor, TabCompleter {

    private SpigotApplicationType application;

    @Override
    public boolean onCommand(CommandSender sender, Command bukkitCommand, String label, String[] args) {
        CommandGroup group = application.getGroup(args);
        ICommand command = group.getCommand();

        if (command != null) {
            try {
                command.call(sender, args);
            } catch (CommandExcpetion ex) {
                String message;
                if (ex instanceof MessageExcepetion) {
                    message = ((MessageExcepetion) ex).getMessage(application);
                } else {
                    message = ex.getMessage();
                }
                sender.sendMessage(application.getPrefix() + message);
            } catch (Throwable ex) {
                sender.sendMessage(application.getPrefix() + "§cError occurred while processing you command");
                if (sender.hasPermission(Permissions.DEBUG)) {
                    sender.sendMessage(ExceptionUtil.toString(ex));
                } else {
                    sender.sendMessage(application.getPrefix() + "§cDu darfst keine dearlirten Details erhalten! Bitte Melde diesen vorfall");
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Tab complete not supported");
        return new ArrayList<>();
    }

}
