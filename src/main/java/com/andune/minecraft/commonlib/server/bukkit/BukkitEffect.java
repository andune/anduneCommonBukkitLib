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
