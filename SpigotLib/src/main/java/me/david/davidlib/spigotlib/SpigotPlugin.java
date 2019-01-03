package me.david.davidlib.spigotlib;

import me.david.davidlib.util.core.application.Application;
import me.david.davidlib.util.core.link.LinkBase;
import me.david.davidlib.spigotlib.exception.PluginLoadException;
import me.david.davidlib.spigotlib.gui.GuiListener;
import me.david.davidlib.spigotlib.link.SpigotLinks;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class SpigotPlugin extends JavaPlugin {

    private static boolean firstPlugin = true;

    @Override
    public void onEnable() {
        try {
            Class.forName("de.splotycode.davidlib.startup.Main").getMethod("mainIfNotInitialised").invoke(null);
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (firstPlugin) {
            firstPlugin = false;
            firstPluginLoad();
        }

        Application rawApplication = LinkBase.getApplicationManager().getApplicationByName(getName());
        if (rawApplication == null) throw new PluginLoadException("Could not find Application with name " + getName());
        if (rawApplication instanceof SpigotApplicationType) throw new PluginLoadException("Application '" + getName() + "' must implements SpigotPluginApplicationType");

        SpigotApplicationType application = (SpigotApplicationType) rawApplication;
        application.getDataFactory().putData(SpigotApplicationType.PLUGIN, this);
    }

    private void firstPluginLoad() {
        LinkBase.getInstance().registerLink(SpigotLinks.MAIN_PLUGIN, this);
        registerListeners(new GuiListener());
    }

    public void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

}
