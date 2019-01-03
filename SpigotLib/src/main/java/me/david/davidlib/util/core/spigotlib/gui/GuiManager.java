package me.david.davidlib.util.core.spigotlib.gui;

import me.david.davidlib.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class GuiManager {

    private HashMap<UUID, InventoryData> openInventorys = new HashMap<>();
    private HashMap<UUID, List<InventoryData>> inventoryHistory = new HashMap<>();

    public InventoryData getInvenctory(UUID uuid) {
        return openInventorys.get(uuid);
    }

    public void openInventory(Player player, Gui gui) {
        InventoryData inventory = new InventoryData(player, gui);
        openInventory(inventory);
    }

    public void openInventory(InventoryData inventory) {
        Player player = inventory.getPlayer();
        String permission = inventory.getGui().getPermission();
        if (!StringUtil.isEmpty(permission) && !player.hasPermission(permission)) {
            player.sendMessage(ChatColor.RED + "Du darfst dieses Inventar nicht öfffnen");
            return;
        }
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3, 2);
        player.openInventory(inventory.getInventory());
    }

    public void endSession(InventoryData session) {
        UUID uuid = session.getPlayer().getUniqueId();
        openInventorys.remove(uuid);
        if (inventoryHistory.containsKey(uuid)) {
            inventoryHistory.get(uuid).add(session);
        } else {
            inventoryHistory.put(uuid, Collections.singletonList(session));
        }
    }

    public InventoryData getLastInventory(Player player) {
        List<InventoryData> list = inventoryHistory.get(player.getUniqueId());
        if (list == null || list.isEmpty()) return null;

        return list.get(list.size() - 1);
    }

    public InventoryData removeLastInventory(Player player) {
        List<InventoryData> list = inventoryHistory.get(player.getUniqueId());
        if (list == null || list.isEmpty()) return null;

        return list.remove(list.size() - 1);
    }

    public HashMap<UUID, InventoryData> getOpenInventorys() {
        return openInventorys;
    }

    public HashMap<UUID, List<InventoryData>> getInventoryHistory() {
        return inventoryHistory;
    }
}
