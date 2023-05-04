package us.thezircon.play.autopickup.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Util {


    public static boolean containsSkulkerBox(List<ItemStack> items) {
        return items.stream()
                .anyMatch(Util::isShulkerBox);
    }

    public static boolean isShulkerBox(ItemStack item) {
        return item.getType().toString().contains("SHULKER_BOX");
    }

    public static boolean containsMaterial(List<ItemStack> items, Material material) {
        return items.stream()
                .anyMatch(i -> i.getType().equals(material));
    }

    public static boolean isStorageBox(ItemStack item) {
        if (item==null)
            return false;
        BoxData data = getData(item.getItemMeta());
        if (data==null)
            return false;
        return data.type==BoxType.STORAGE;
    }

    public static boolean isTrashBox(ItemStack item) {
        if (item==null)
            return false;
        BoxData data = getData(item.getItemMeta());
        if (data==null)
            return false;
        return data.type==BoxType.TRASH;
    }

    public static BoxData getData(ItemMeta meta) {
        if (meta==null) return null;
        if (!meta.getPersistentDataContainer().has(new NamespacedKey("shulkerbox", "box-data"), PersistentDataType.STRING)) return null;
        String data = meta.getPersistentDataContainer().get(new NamespacedKey("shulkerbox", "box-data"), PersistentDataType.STRING);
        if (data==null) return null;
        return BoxData.deserialize(data);
    }

    public static void setData(ItemStack item, BoxData data) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey("shulkerbox", "box-data"), PersistentDataType.STRING, data.serialize());
        item.setItemMeta(meta);
    }

    public static void removeData(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(new NamespacedKey("shulkerbox", "box-data"));
        item.setItemMeta(meta);
    }

    public static void updateShulker(ItemStack item) {
        BoxData data = getData(item.getItemMeta());

        if (data==null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§r§fシュルカーボックス");
            meta.setLore(new ArrayList<>());
            meta.removeItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
        } else {
            ItemMeta meta = item.getItemMeta();
            // TODO
            if (data.type==BoxType.STORAGE)
                meta.setDisplayName("§2自動収納ボックス");
            else if (data.type==BoxType.TRASH)
                meta.setDisplayName("§4自動ゴミ箱");
            List<String> lore = new ArrayList<>();
            if (data.type==BoxType.STORAGE)
                lore.add("§r§a収納アイテム:");
            else if (data.type==BoxType.TRASH)
                lore.add("§r§cゴミアイテム:");
            for (Material mat : data.mats) {
                lore.add("§r§f- "+mat.name());
            }
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
        }
    }
}
