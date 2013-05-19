/**
 * 
 */
package com.andune.minecraft.commonlib.server.bukkit;

import java.util.HashMap;
import java.util.Map;

import com.andune.minecraft.commonlib.server.api.Sound;

/**
 * Enum that maps API sounds to their Bukkit equivalents.
 * 
 * @author andune
 *
 */
public enum BukkitSound {
    THUNDER(Sound.THUNDER, org.bukkit.Sound.AMBIENCE_THUNDER),
    ENDERMAN_TELEPORT(Sound.ENDERMAN_TELEPORT, org.bukkit.Sound.ENDERMAN_TELEPORT),
    FIRE(Sound.FIRE, org.bukkit.Sound.FIRE),
    EXPLODE(Sound.EXPLODE, org.bukkit.Sound.EXPLODE),
    FIZZ(Sound.FIZZ, org.bukkit.Sound.FIZZ),
    PORTAL_TRIGGER(Sound.PORTAL_TRIGGER, org.bukkit.Sound.PORTAL_TRIGGER),
    ENDERDRAGON_GROWL(Sound.ENDERDRAGON_GROWL, org.bukkit.Sound.ENDERDRAGON_GROWL);
    
    private static final Map<Sound, org.bukkit.Sound> soundMap = new HashMap<Sound, org.bukkit.Sound>();
    private Sound apiSound;
    private org.bukkit.Sound bukkitSound;
    
    private BukkitSound(Sound apiSound, org.bukkit.Sound bukkitSound) {
        this.apiSound = apiSound;
        this.bukkitSound = bukkitSound;
    }
    
    public static org.bukkit.Sound getBukkitSound(Sound apiSound) {
        return soundMap.get(apiSound);
    }
    
    static {
        for(BukkitSound sound : values()) {
            soundMap.put(sound.apiSound, sound.bukkitSound);
        }
    }

}
