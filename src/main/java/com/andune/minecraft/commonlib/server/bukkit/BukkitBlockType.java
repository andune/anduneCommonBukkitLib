package com.andune.minecraft.commonlib.server.bukkit;

import com.andune.minecraft.commonlib.server.api.BlockType;
import com.andune.minecraft.commonlib.server.api.BlockTypes;
import org.bukkit.Material;

import java.util.HashMap;

/**
 * @author andune
 */
public class BukkitBlockType implements BlockType {
    private final static HashMap<BlockTypes, BukkitBlockType> forwardLookup = new HashMap<BlockTypes, BukkitBlockType>();
    private final static HashMap<Material, BukkitBlockType> reverseLookup = new HashMap<Material, BukkitBlockType>();
    static {
        for(BlockTypes type : BlockTypes.values()) {
            final Material material = Material.getMaterial(type.toString());
            final BukkitBlockType bbt = new BukkitBlockType(type, material);

            forwardLookup.put(type, bbt);
            reverseLookup.put(material, bbt);
        }
    }

    private final Material material;
    private final BlockTypes blockType;

    /**
     * For internal use during initialization only.
     *
     * @param blockType
     * @param material
     */
    private BukkitBlockType(BlockTypes blockType, Material material) {
        this.blockType = blockType;
        this.material = material;
    }

    /*
    public BukkitBlockType(BlockTypes blockType) {
        this.blockType = blockType;
        this.material = forwardLookup.get(blockType).getMaterial();
    }
    public BukkitBlockType(Material material) {
        this.material = material;
        this.blockType = reverseLookup.get(material).getBlockType();
    }
    */

    public boolean equals(Object o) {
        if(o == null) return false;
        if((o instanceof BukkitBlockType) == false) return false;
        final BukkitBlockType bbt = (BukkitBlockType) o;
        return bbt.blockType == blockType;
    }
    public int hashCode() {
        return blockType.hashCode();
    }

    @Override
    public BlockTypes getBlockType() {
        return blockType;
    }

    // Bukkit-specific method
    public Material getMaterial() {
        return material;
    }

    public static BlockType getBlockType(Material material) {
        return reverseLookup.get(material);
    }
    public static Material getMaterial(BlockTypes blockType) {
        return forwardLookup.get(blockType).material;
    }
}
