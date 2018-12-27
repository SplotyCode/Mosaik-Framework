package me.david.davidlib.spigotlib;

import me.david.davidlib.application.ApplicationType;
import me.david.davidlib.datafactory.DataKey;
import me.david.davidlib.spigotlib.gui.GuiManager;
import me.david.davidlib.startup.BootContext;
import org.bukkit.event.Listener;

public interface SpigotApplicationType extends ApplicationType {

    DataKey<SpigotPlugin> PLUGIN = new DataKey<>("spigot.plugin");
    GuiManager GUI_MANAGER = new GuiManager();

    default void initType(BootContext context, SpigotApplicationType dummy) {

    }

    default SpigotPlugin getPlugin() {
        return getData(PLUGIN);
    }

    default void registerListeners(Listener... listeners) {
        getPlugin().registerListeners(listeners);
    }

}
