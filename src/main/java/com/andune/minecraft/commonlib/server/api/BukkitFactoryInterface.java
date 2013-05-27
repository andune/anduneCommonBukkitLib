/**
 * 
 */
package com.andune.minecraft.commonlib.server.api;

import com.andune.minecraft.commonlib.server.bukkit.BukkitPlayer;

/**
 * @author andune
 *
 */
public interface BukkitFactoryInterface extends Factory {
    public BukkitPlayer newBukkitPlayer(org.bukkit.entity.Player bukkitPlayer);
}
