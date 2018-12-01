package me.david.davidlib.spigotlib.gui;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.datafactory.DataFactory;
import me.david.davidlib.datafactory.DataKey;
import me.david.davidlib.helper.AlmostBoolean;
import me.david.davidlib.spigotlib.SpigotApplicationType;
import me.david.davidlib.spigotlib.util.ColorCodes;
import me.david.davidlib.spigotlib.util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryData {

    public static final DataKey<Integer> PAGE = new DataKey<>("lib.page");

    @Getter private Player player;
    @Getter private Gui gui;
    private List<ItemStack> content;
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
        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return false;
        }
        if (closeable.isCloseButton() && event.getRawSlot() == event.getClickedInventory().getSize() - 1) {
            player.closeInventory();
            SpigotApplicationType.getGuiManager().endSession(this);
            return true;
        }
        return gui.onItemClick(player, this, event);
    }

    public ItemStack[] getContent() {
        if(!factory.containsData(PAGE)) factory.putData(PAGE, 0);
        int currentPage = factory.getData(PAGE);
        boolean page = isPage();
        int size = getSize();

        int startCount = currentPage*45;

        ItemStack[] inv = new ItemStack[size];

        int i = 0;
        for (int count = startCount;count < startCount+size && count != content.size();count++) {
            inv[i] = content.get(count);
            i++;
        }


        if (page) {
            if (currentPage != 0) inv[46] = ItemStackUtil.createbasic("§6Letzte Seite", currentPage-1, Material.ARROW);
            if (content.size() > startCount + size) inv[size - 2] = ItemStackUtil.createbasic("§6Nägste Seite", currentPage+1, Material.ARROW);
        }

        Closeable closeable = getCloseable();

        if (closeable.isCloseButton()) {
            inv[size - 1] = ItemStackUtil.createColoredWoolLohre("Exit", 1, ColorCodes.RED.getId());
        }

        return inv;
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
        size = isPage() ? 45 : (int) Math.ceil((content.size()+1D) / 9D)*9;
        if (size < 9) size = 9;
    }

    public boolean isPage() {
        if (page == AlmostBoolean.MAYBE) {
            page = AlmostBoolean.fromBoolean(content.size() + 1 > 54);
        }
        return page.decide(false);
    }

    public Inventory getInventory() {
        if (inventory == null) {
            recaclcSize();
            inventory = Bukkit.createInventory(null, getSize(), getDisplayName());
            inventory.setContents(getContent());
        }
        return inventory;
    }

    public InventoryData setItem(int index, ItemStack itemStack) {
        if (content == null) content = new ArrayList<>();
        content.set(index, itemStack);
        return this;
    }

    public InventoryData addItems(ItemStack... items) {
        if (content == null) {
            content = Arrays.asList(items);
            return this;
        }
        Collections.addAll(content, items);
        return this;
    }

    public InventoryData setList(List<ItemStack> content) {
        this.content = content;
        return this;
    }

    public DataFactory getFactory() {
        if (factory == null) factory = new DataFactory();
        return factory;
    }
}
