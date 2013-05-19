/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2013 Andune (andune.alleria@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
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
