package io.github.splotycode.mosaik.spigotlib;

import io.github.splotycode.mosaik.runtime.application.ApplicationType;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.spigotlib.gui.GuiManager;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
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
