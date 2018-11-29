package me.david.davidlib.spigotlib;

import de.splotycode.davidlib.startup.Main;
import me.david.davidlib.application.Application;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.spigotlib.exception.PluginLoadException;
import me.david.davidlib.spigotlib.gui.GuiListener;
import me.david.davidlib.spigotlib.link.SpigotLinks;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class SpigotPlugin extends JavaPlugin {

    private static boolean firstPlugin = true;

    @Override
    public void onEnable() {
        Main.mainIfNotInitialised();
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
