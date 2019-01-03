package me.david.davidlib.util.core.spigotlib.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ItemStackUtil {

    /*
     * Create basic Itemstack
     * Displayname, Amount, Material
     */
    public static ItemStack createbasic(String name, int amount, Material material) {
        ItemStack is = new ItemStack(material, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return is;
    }

    /*
     * Create PlayerHead Itemstack
     * Displayname, Amount, Playername
     */
    public static ItemStack createPlayerHead(String name, int amount, String player) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        sm.setDisplayName(name);
        sm.setOwner(player);
        is.setItemMeta(sm);
        return is;
    }

    /*
     * Create PlayerHead Itemstack
     * Displayname, Amount, Playerobjekt
     */
    public static ItemStack createPlayerHead(String name, int amount, Player player) {
        return createPlayerHead(name, amount, player.getName());
    }

    /*
     * Lade einen Kopf von der mhf Datenbank
     * (http://minecraftplayerheadsdatabase.weebly.com/mobsanimals.html)
     */
    public static ItemStack createAnimalHead(String name, int amount, String mhfname) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        sm.setDisplayName(name);
        sm.setOwner("MHF_" + name);
        is.setItemMeta(sm);
        return is;
    }

    /*
     * Create Lohre Itemstack
     * Displayname, Amount, material, lohre
     */
    public static ItemStack createLohresimple(String name, int amount, Material material, String lohre1) {
        List<String> lohre = new ArrayList<>();
        lohre.add(lohre1);
        ItemStack is = new ItemStack(material, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lohre);
        is.setItemMeta(im);
        return is;
    }

    /*
     * Create Lohre Itemstack
     * Displayname, Amount, material, lohre...
     */
    public static ItemStack createLohre(String name, int amount, Material material, String... lohre) {
        return createLohre(name, amount, material, Arrays.asList(lohre));
    }

    /*
     * Create Lohre Itemstack
     * Displayname, Amount, material, lohrelist
     */
    public static ItemStack createLohre(String name, int amount, Material material, List<String> lohre) {
        ItemStack is = new ItemStack(material, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lohre);
        is.setItemMeta(im);
        return is;
    }

    /*
     * Create cacel ItemStack
     */
    public static ItemStack createcancel() {
        return createbasic("§cAbbrechen", 1, Material.BARRIER);
    }

    /*
     * Create cacel direct in inventory
     */
    public static Inventory createcancel(Inventory inv) {
        inv.addItem(createcancel());
        return inv;
    }

    /*
     * Create ColoredWool Itemstack
     * Displayname, Amount, color
     */
    public static ItemStack createColoredWool(String name, int amount, int color) {
        ItemStack is = new ItemStack(Material.WOOL, amount, (byte) color);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return is;
    }

    /*
     * Create ColoredWool Itemstack
     * Displayname, Amount, color as boolean
     */
    public static ItemStack createColoredWool(String name, int amount, boolean color) {
        ItemStack is = new ItemStack(Material.WOOL, amount, (short) (color?ColorCodes.LIME.getId():ColorCodes.RED.getId()));
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return is;
    }

    /*
     * Create PlayerHead Itemstack
     * Displayname, Amount, Playername, Lohre
     */
    public static ItemStack createPlayerHeadLohre(String name, int amount, String player, String... lohre) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        sm.setDisplayName(name);
        sm.setOwner(player);
        sm.setLore(Arrays.asList(lohre));
        is.setItemMeta(sm);
        return is;
    }

    /*
     * Create ColoredStainedGlass Itemstack
     * Displayname, Amount, color
     */
    public static ItemStack createStanedGlassPane(String name, int amount, int color) {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, amount, (byte) color);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return is;
    }

    /*
     * Create ColoredStainedGlass Itemstack
     * Displayname, Amount, short, Material, Lohre
     */
    public static ItemStack createshortlohre(String name, int amount, int dura, Material material, String... lohre) {
        ItemStack is = new ItemStack(material, amount, (short) dura);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lohre));
        is.setItemMeta(im);
        return is;
    }

    /*
     * Create ColoredWool Itemstack
     * Displayname, Amount, color, lohre
     */
    public static ItemStack createColoredWoolLohre(String name, int amount, int color, String... lohre) {
        ItemStack is = new ItemStack(Material.WOOL, amount, (byte) color);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lohre));
        is.setItemMeta(im);
        return is;
    }

    /*
     * Create Posion Itemstack
     */
    public static ItemStack createPotion(int level, int number, int amoumt, String displayName) {
        ItemStack potion = new ItemStack(Material.POTION, amoumt, (short) number);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setDisplayName(displayName);
        potion.setItemMeta(meta);
        return potion;
    }

    /*
     * Create Posion Itemstack with lohre
     */
    public static ItemStack createPotionLohre(int level, int number, int amoumt, String displayName, String... lohre) {
        ItemStack potion = new ItemStack(Material.POTION, amoumt, (short) number);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(lohre));
        potion.setItemMeta(meta);
        return potion;
    }

}
