package net.pravian.aero.serializable;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents a serializable inventory.
 *
 * @see SerializableObject
 */
public class SerializableInventory extends SerializableObject<Inventory> {

    private final Inventory inventory;
    private final String serialized;

    /**
     * Creates a new SerializableInventory instance.
     *
     * @param inventory The Inventory to be serialized.
     */
    public SerializableInventory(Inventory inventory) {
        String newSerialized = inventory.getSize() + ";";

        try {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack is = inventory.getItem(i);
                if (is != null) {
                    String serializedItemStack = new String();

                    String isType = String.valueOf(is.getType().toString());
                    serializedItemStack += "t@" + isType;

                    ItemMeta meta = is.getItemMeta();
                    if (meta instanceof Damageable) {
                        Damageable damageable = (Damageable) meta;
                        if (damageable.getDamage() != 0) {
                            String isDurability = String.valueOf(damageable.getDamage());
                            serializedItemStack += ":d@" + isDurability;
                        }
                    }

                    if (is.getAmount() != 1) {
                        String isAmount = String.valueOf(is.getAmount());
                        serializedItemStack += ":a@" + isAmount;
                    }

                    Map<Enchantment, Integer> isEnch = is.getEnchantments();
                    if (isEnch.size() > 0) {
                        for (Entry<Enchantment, Integer> ench : isEnch.entrySet()) {
                            serializedItemStack += ":e@" + ench.getKey().getKey().toString() + "@" + ench.getValue();
                        }
                    }

                    newSerialized += i + "#" + serializedItemStack + ";";
                }
            }
        } catch (Exception ex) {
            newSerialized = null;
        }

        this.inventory = inventory;
        this.serialized = newSerialized;
    }

    /**
     * Creates a new SerializableEntityLocation instance.
     *
     * @param inventory The String to be serialized.
     */
    public SerializableInventory(String inventory) {
        String[] serializedBlocks = inventory.split(";");
        String invInfo = serializedBlocks[0];
        Inventory newInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invInfo));

        try {

            for (int i = 1; i < serializedBlocks.length; i++) {
                String[] serializedBlock = serializedBlocks[i].split("#");
                int stackPosition = Integer.valueOf(serializedBlock[0]);

                if (stackPosition >= newInventory.getSize()) {
                    continue;
                }

                ItemStack is = null;
                Boolean createdItemStack = false;

                String[] serializedItemStack = serializedBlock[1].split(":");
                for (String itemInfo : serializedItemStack) {
                    String[] itemAttribute = itemInfo.split("@");
                    if (itemAttribute[0].equals("t")) {
                        Material material = Material.matchMaterial(itemAttribute[1]);
                        if (material != null) {
                            is = new ItemStack(material);
                            createdItemStack = true;
                        }
                    } else if (is == null) {
                        // Quick return to avoid NPE
                    } else if (itemAttribute[0].equals("d") && createdItemStack) {
                        ItemMeta meta = is.getItemMeta();
                        if (meta instanceof Damageable) {
                            Damageable damageable = (Damageable) meta;
                            damageable.setDamage(Short.valueOf(itemAttribute[1]));
                            is.setItemMeta(meta);
                        }
                    } else if (itemAttribute[0].equals("a") && createdItemStack) {
                        is.setAmount(Integer.valueOf(itemAttribute[1]));
                    } else if (itemAttribute[0].equals("e") && createdItemStack) {
                        String[] split = itemAttribute[1].split(":");
                        if (split.length == 2) {
                            NamespacedKey key = new NamespacedKey(split[0], split[1]);
                            Enchantment e = org.bukkit.Registry.ENCHANTMENT.get(key);
                            if (e != null) {
                                is.addEnchantment(e, Integer.valueOf(itemAttribute[2]));
                            }
                        }
                    }
                }
                newInventory.setItem(stackPosition, is);
            }

        } catch (Exception ex) {
            newInventory = null;
        }

        this.inventory = newInventory;
        this.serialized = inventory;
    }

    @Override
    public String serialize() {
        return serialized;
    }

    @Override
    public Inventory deserialize() {
        return inventory;
    }
}
