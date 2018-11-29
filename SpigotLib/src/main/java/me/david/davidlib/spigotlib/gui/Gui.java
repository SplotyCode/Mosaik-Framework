package me.david.davidlib.spigotlib.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface Gui {

    void onPreOpen(Player player, InventoryData data);
    default void onPostOpen(Player player, Inventory inventory) {}

    default void onClose(Player player, InventoryData data) {}

    boolean onItemClick(Player player, InventoryData inventory, InventoryClickEvent event);

}
