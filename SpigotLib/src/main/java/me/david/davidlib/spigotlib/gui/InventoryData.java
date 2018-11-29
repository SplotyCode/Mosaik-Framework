package me.david.davidlib.spigotlib.gui;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.datafactory.DataFactory;
import me.david.davidlib.helper.AlmostBoolean;
import me.david.davidlib.spigotlib.gui.close.Closeable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryData {

    @Getter private Player player;
    @Getter private Gui gui;
    private ItemStack[] content;
    private String displayName = null;
    private int size = -1;
    private DataFactory factory;
    @Setter private Inventory inventory;
    private Closeable closeable;
    private AlmostBoolean page = AlmostBoolean.MAYBE;

    public InventoryData(Player player, Gui gui) {
        this.player = player;
        this.gui = gui;
    }

    public boolean itemClick(InventoryClickEvent event) {
        return gui.onItemClick(player, this, event);
    }

    public ItemStack[] getContent() {
        return content;
    }

    public void setPage(boolean page) {
        this.page = AlmostBoolean.fromBoolean(page);
    }

    public InventoryData setCloseable(Closeable closeable) {
        this.closeable = closeable;
        return this;
    }

    public Closeable getCloseable() {
        if (closeable == null) {
            closeable = Closeable.ALL;
        }
        return closeable;
    }

    public int getSize() {
        if (size == -1) {
            recaclcSize();
        }
        return size;
    }

    public String getDisplayName() {
        if (displayName == null) {
            displayName = "Unamed";
        }
        return displayName;
    }

    public void recaclcSize() {
        size = isPage() ? 45 : (int) Math.ceil((content.length+1D) / 9D)*9;
        if (size < 9) size = 9;
    }

    public boolean isPage() {
        if (page == AlmostBoolean.MAYBE) {
            page = AlmostBoolean.fromBoolean(content.length + 1 > 54);
        }
        return page.decide(false);
    }

    public Inventory getInventory() {
        if (inventory == null) {
            recaclcSize();
            inventory = Bukkit.createInventory(null, getSize(), getDisplayName());
        }
        return inventory;
    }

    public DataFactory getFactory() {
        if (factory == null) factory = new DataFactory();
        return factory;
    }
}
