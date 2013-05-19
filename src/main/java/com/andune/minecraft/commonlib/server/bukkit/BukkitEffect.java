/**
 * 
 */
package com.andune.minecraft.commonlib.server.bukkit;

import java.util.HashMap;
import java.util.Map;

import com.andune.minecraft.commonlib.server.api.Effect;

/**
 * Enum that maps API effects to their Bukkit equivalents.
 * 
 * @author andune
 *
 */
public enum BukkitEffect {
    SMOKE(Effect.SMOKE, org.bukkit.Effect.SMOKE),
    POTION_BREAK(Effect.POTION_BREAK, org.bukkit.Effect.POTION_BREAK),
    ENDER_SIGNAL(Effect.ENDER_SIGNAL, org.bukkit.Effect.ENDER_SIGNAL),
    MOBSPAWNER_FLAMES(Effect.MOBSPAWNER_FLAMES, org.bukkit.Effect.MOBSPAWNER_FLAMES);
    
    private static final Map<Effect, org.bukkit.Effect> effectMap = new HashMap<Effect, org.bukkit.Effect>();
    private Effect apiEffect;
    private org.bukkit.Effect bukkitEffect;
    
    private BukkitEffect(Effect apiEffect, org.bukkit.Effect bukkitEffect) {
        this.apiEffect = apiEffect;
        this.bukkitEffect = bukkitEffect;
    }
    
    public static org.bukkit.Effect getBukkitEffect(Effect apiEffect) {
        return effectMap.get(apiEffect);
    }
    
    static {
        for(BukkitEffect effect : values()) {
            effectMap.put(effect.apiEffect, effect.bukkitEffect);
        }
    }
}
