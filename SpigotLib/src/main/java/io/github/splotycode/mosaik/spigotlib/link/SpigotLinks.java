package io.github.splotycode.mosaik.spigotlib.link;

import io.github.splotycode.mosaik.util.datafactory.DataKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.plugin.PluginBase;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotLinks {

    public static final DataKey<PluginBase> MAIN_PLUGIN = new DataKey<>("spigot.plugin");

}
