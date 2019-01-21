package io.github.splotycode.mosaik.spigotlib.gui;

import io.github.splotycode.mosaik.spigotlib.SpigotApplicationType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class GuiListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        GuiManager manager = SpigotApplicationType.GUI_MANAGER;
        UUID uuid = event.getPlayer().getUniqueId();

        InventoryData inventory = manager.getOpenInventorys().get(uuid);

        if (inventory != null) {
            event.getPlayer().openInventory(inventory.getInventory());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        GuiManager manager = SpigotApplicationType.GUI_MANAGER;
        UUID uuid = event.getPlayer().getUniqueId();

        InventoryData inventory = manager.getOpenInventorys().get(uuid);

        if (inventory != null) {
            Closeable closeable = inventory.getCloseable();
            if (closeable.isQuit()) {
                manager.endSession(inventory);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        GuiManager manager = SpigotApplicationType.GUI_MANAGER;
        UUID uuid = event.getWhoClicked().getUniqueId();

        InventoryData inventory = manager.getOpenInventorys().get(uuid);

        if (inventory != null) {
            boolean result = inventory.itemClick(event);
            if (result) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        GuiManager manager = SpigotApplicationType.GUI_MANAGER;
        UUID uuid = event.getPlayer().getUniqueId();

        InventoryData inventory = manager.getOpenInventorys().get(uuid);

        if (inventory != null) {
            Closeable closeable = inventory.getCloseable();
            if (closeable.isClose()) {
                manager.endSession(inventory);
            }
        }
    }

}
