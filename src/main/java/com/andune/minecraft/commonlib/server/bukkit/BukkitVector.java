package com.andune.minecraft.commonlib.server.bukkit;

import com.andune.minecraft.commonlib.server.api.Vector;

/**
 * @author andune
 */
public class BukkitVector implements Vector {
    private final org.bukkit.util.Vector bukkitVector;

    public BukkitVector(double x, double y, double z) {
        bukkitVector = new org.bukkit.util.Vector(x, y, z);
    }

    public BukkitVector(int x, int y, int z) {
        bukkitVector = new org.bukkit.util.Vector(x, y, z);
    }

    public org.bukkit.util.Vector getBukkitVector() {
        return bukkitVector;
    }
}
