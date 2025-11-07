package net.pravian.aero.util;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Represents all Inventory-related utilities.
 */
public class Inventories {

    private Inventories() {
    }

    /**
     * Gets the total item count of a given type and data
     *
     * <p>
     * Author: bergerkiller</p>
     *
     * @param inventory to look in
     * @param typeid of the items to look for, -1 for any item
     * @param data of the items to look for, -1 for any data
     * @return Amount of items in the inventory
     */
    public static int getItemCount(Inventory inventory, int typeid, int data) {
        if (typeid < 0) {
            int count = 0;
            for (ItemStack item : inventory.getContents()) {
                if (item != null) {
                    count += item.getAmount();
                }
            }
            return count;
        } else {
            org.bukkit.inventory.ItemStack rval = findItem(inventory, typeid, data);
            return rval == null ? 0 : rval.getAmount();
        }
    }

    /**
     * Obtains an item of the given type and data in the inventory specified<br>
     * If multiple items with the same type and data exist, their amounts are added together
     *
     * <p>
     * Author: bergerkiller</p>
     *
     * @param inventory to look in
     * @param typeId of the items to look for, -1 for any item (deprecated: Material IDs no longer supported)
     * @param data of the items to look for, -1 for any data (deprecated: Data values no longer supported)
     * @return Amount of items in the inventory
     * @deprecated Material IDs and data values are no longer supported. Use Material-based methods instead.
     */
    @Deprecated
    public static ItemStack findItem(Inventory inventory, int typeId, int data) {
        // Material IDs are no longer supported in modern Minecraft
        // This method is kept for backwards compatibility but will not work correctly
        // Consider using Material-based inventory methods instead
        if (typeId == -1) {
            // Return any item if typeId is -1
            for (ItemStack item : inventory.getContents()) {
                if (item != null) {
                    return item.clone();
                }
            }
            return null;
        }
        
        // Since Material IDs are not supported, we cannot match by typeId
        // This method will not work as expected in modern versions
        return null;
    }

    /**
     * Adds a certain amount to an item, without limiting to the max stack size
     *
     * <p>
     * Author: bergerkiller</p>
     *
     * @param item
     * @param amount to add
     */
    public static void addAmount(ItemStack item, int amount) {
        item.setAmount(Math.max(item.getAmount() + amount, 0));
    }

    /**
     * Validates if an inventory contains at certain amount of items from the specified material.
     *
     * @param inventory The Inventory to validate
     * @param material The material to match to
     * @param minAmount The amount of items to validate
     *
     * @return If the inventory contains that amount of items.
     */
    public static boolean containsAtLeast(Inventory inventory, Material material, int minAmount) {
        int amount = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item == null) {
                continue;
            }

            if (item.getType() == material) {
                amount += item.getAmount();
            }
        }

        return amount >= minAmount;
    }

    /**
     * Closes the inventory for all HumanEntity's viewing the inventory
     *
     * @param inventory The inventory to close
     */
    public static void closeInventory(Inventory inventory) {
        for (LivingEntity entity : inventory.getViewers()) {
            if (entity instanceof HumanEntity) {
                ((HumanEntity) entity).closeInventory();
            }
        }
    }
}
