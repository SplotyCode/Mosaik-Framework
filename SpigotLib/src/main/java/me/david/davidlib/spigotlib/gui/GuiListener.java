package me.david.davidlib.spigotlib.gui;

import me.david.davidlib.spigotlib.SpigotApplicationType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class GuiListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        GuiManager manager = SpigotApplicationType.getGuiManager();
        UUID uuid = event.getPlayer().getUniqueId();

        InventoryData inventory = manager.getOpenInventorys().get(uuid);

        if (inventory != null) {
            event.getPlayer().openInventory(inventory.getInventory());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        GuiManager manager = SpigotApplicationType.getGuiManager();
        UUID uuid = event.getWhoClicked().getUniqueId();

        InventoryData inventory = manager.getOpenInventorys().get(uuid);

        if (inventory != null) {
            boolean result = inventory.itemClick(event);
            if (result) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

    }

}
