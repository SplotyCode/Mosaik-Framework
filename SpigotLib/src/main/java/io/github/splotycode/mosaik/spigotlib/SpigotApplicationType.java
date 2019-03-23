package io.github.splotycode.mosaik.spigotlib;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.application.ApplicationType;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.spigotlib.command.CommandGroup;
import io.github.splotycode.mosaik.spigotlib.command.CommandRedirect;
import io.github.splotycode.mosaik.spigotlib.gui.GuiManager;
import io.github.splotycode.mosaik.spigotlib.locale.SpigotLocale;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.i18n.I18N;
import org.bukkit.event.Listener;

import java.io.File;

public interface SpigotApplicationType extends ApplicationType {

    DataKey<SpigotPlugin> PLUGIN = new DataKey<>("spigot.plugin");
    DataKey<String> PREFIX = new DataKey<>("spigot.prefix");

    DataKey<CommandGroup> COMMAND_HEAD = new DataKey<>("spigot.command_head");
    DataKey<CommandRedirect> COMMAND_REDIRECT = new DataKey<>("spigot.redirect");

    DataKey<I18N> I18N = new DataKey<>("spigot.i18n");

    GuiManager GUI_MANAGER = new GuiManager();

    default CommandGroup getGroup(String[] name) {
        CommandGroup current = getCommandHead();
        for (String sub : name) {
            CommandGroup subGroup = current.getChilds().get(sub);
            if (subGroup == null) return current;
            current = subGroup;
        }
        return current;
    }

    default void initType(BootContext context, SpigotApplicationType dummy) {
        putData(COMMAND_HEAD, new CommandGroup.Head("", this));
        putData(COMMAND_REDIRECT, new CommandRedirect(this));
    }

    default void useLangeageFile(String name) {
        File file = new File(LinkBase.getInstance().getLink(Links.PATH_MANAGER).getMainDirectory(), name);
        putData(I18N, new I18N().setLocale(new SpigotLocale(file)));
    }

    default CommandGroup getCommandHead() {
        return getData(COMMAND_HEAD);
    }

    default SpigotPlugin getPlugin() {
        return getData(PLUGIN);
    }

    default String getPrefix() {
        return getData(PREFIX);
    }

    default I18N getI18N() {
        return getData(I18N);
    }

    default void setPrefix(String prefix) {
        putData(PREFIX, prefix);
    }

    default void registerListeners(Listener... listeners) {
        getPlugin().registerListeners(listeners);
    }

}
