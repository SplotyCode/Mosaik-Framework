package me.david.davidlib.spigotlib.gui;

import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuiManager {

    @Getter private HashMap<UUID, InventoryData> openInventorys = new HashMap<>();
    @Getter private HashMap<UUID, List<InventoryData>> inventoryHistory = new HashMap<>();

    public InventoryData getInvenctory(UUID uuid) {
        return openInventorys.get(uuid);
    }

    public void openInventory(Player player, Gui gui) {
        InventoryData inventory = new InventoryData(player, gui);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3, 2);
        player.openInventory(inventory.getInventory());
    }

}
