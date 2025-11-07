package net.pravian.aero.util;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class Materials {

    /**
     * <p>
     * Author: Essentials Team</p>
     */
    public static final HashSet<Material> TRANSPARENT_MATERIALS = new HashSet<Material>();

    static {
        // Materials that are transparent - using BlockData API instead of deprecated isTransparent()
        for (Material mat : Material.values()) {
            if (mat.isBlock()) {
                try {
                    BlockData blockData = mat.createBlockData();
                    if (blockData != null && !blockData.getMaterial().isOccluding()) {
                        TRANSPARENT_MATERIALS.add(mat);
                    }
                } catch (Exception ignored) {
                    // Material might not support block data, skip it
                }
            }
        }

        // Add water types
        TRANSPARENT_MATERIALS.addAll(Enums.getAllMatching(Material.class, "WATER", "FLOWING_WATER"));
    }

    private Materials() {
    }

    /**
     * Returns all transparent materials
     *
     * @return the materials
     * @deprecated Use TRANSPARENT_MATERIALS
     */
    public static Set<Material> getTransparentMaterials() {
        return TRANSPARENT_MATERIALS;
    }
}
