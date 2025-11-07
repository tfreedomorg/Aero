package net.pravian.aero.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents all ItemStack-related utilities.
 */
public class Items {

    private Items() {
    }

    public static boolean isSameType(ItemStack item, ItemStack compare) {
        return item.getType() == compare.getType();
    }

    /**
     * Checks if two items are the same type and have the same damage/durability.
     * Note: In modern Minecraft, damage is stored in ItemMeta rather than data values.
     */
    public static boolean isSameTypeAndDamage(ItemStack item, ItemStack compare) {
        if (!isSameType(item, compare)) {
            return false;
        }
        
        // Compare damage values using Damageable interface
        ItemMeta itemMeta = item.getItemMeta();
        ItemMeta compareMeta = compare.getItemMeta();
        
        if (itemMeta instanceof Damageable && compareMeta instanceof Damageable) {
            Damageable itemDamageable = (Damageable) itemMeta;
            Damageable compareDamageable = (Damageable) compareMeta;
            return itemDamageable.getDamage() == compareDamageable.getDamage();
        }
        
        // If neither has damage metadata, they're considered equal
        return !(itemMeta instanceof Damageable) && !(compareMeta instanceof Damageable);
    }

    public static boolean isEqual(ItemStack item, ItemStack compare) {
        return isSameTypeAndDamage(item, compare) && item.getAmount() == compare.getAmount();
    }
}
