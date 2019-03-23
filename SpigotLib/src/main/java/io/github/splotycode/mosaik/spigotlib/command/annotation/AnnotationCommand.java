package io.github.splotycode.mosaik.spigotlib.command.annotation;

import io.github.splotycode.mosaik.spigotlib.command.annotation.annotations.Command;
import io.github.splotycode.mosaik.spigotlib.command.annotation.annotations.NeedPermission;
import io.github.splotycode.mosaik.spigotlib.command.command.ICommand;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.List;

public class AnnotationCommand implements ICommand {

    private Command command;
    private Method method;
    private Object object;
    private String permission = null;
    private Class clazz;

    public AnnotationCommand(Command command, Method method, Object object) {
        this.command = command;
        this.method = method;
        this.object = object;
        clazz = object.getClass();
        NeedPermission permissionAnno = ((NeedPermission)clazz.getAnnotation(NeedPermission.class));
        if (permissionAnno != null) permission = permissionAnno.value();
    }

    @Override
    public void call(CommandSender sender, String[] args) throws Exception {
        if (permission != null && !sender.hasPermission(permission)) throw new CommandException("Du hast keine recht dafür");

    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public String desciption() {
        return null;
    }

    @Override
    public String usage() {
        return null;
    }
}
