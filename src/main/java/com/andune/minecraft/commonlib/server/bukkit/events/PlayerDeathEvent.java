package com.andune.minecraft.commonlib.server.bukkit.events;

import com.andune.minecraft.commonlib.server.bukkit.BukkitFactory;

/**
 * @author andune
 *
 */
public class PlayerDeathEvent extends PlayerEvent
        implements com.andune.minecraft.commonlib.server.api.events.PlayerDeathEvent
{
    public PlayerDeathEvent(org.bukkit.entity.Player player, BukkitFactory bukkitFactory) {
        super(player, bukkitFactory);
    }
}
