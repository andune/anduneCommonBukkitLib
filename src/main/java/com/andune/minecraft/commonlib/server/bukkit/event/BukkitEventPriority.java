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
package com.andune.minecraft.commonlib.server.bukkit.event;

import java.util.HashMap;

import com.andune.minecraft.commonlib.server.api.event.EventPriority;

/**
 * @author andune
 *
 */
public enum BukkitEventPriority {
    LOWEST(EventPriority.LOWEST, org.bukkit.event.EventPriority.LOWEST),
    LOW(EventPriority.LOW, org.bukkit.event.EventPriority.LOW),
    NORMAL(EventPriority.NORMAL, org.bukkit.event.EventPriority.NORMAL),
    HIGH(EventPriority.HIGH, org.bukkit.event.EventPriority.HIGH),
    HIGHEST(EventPriority.HIGHEST, org.bukkit.event.EventPriority.HIGHEST),
    MONITOR(EventPriority.MONITOR, org.bukkit.event.EventPriority.MONITOR);
    
    private static final HashMap<EventPriority, org.bukkit.event.EventPriority> priorityMap = new HashMap<EventPriority, org.bukkit.event.EventPriority>(10);
    static {
        for(BukkitEventPriority p: BukkitEventPriority.values()) {
            priorityMap.put(p.getPriority(), p.getBukkitPriority());
        }
    }    
    private EventPriority apiPriority;
    private org.bukkit.event.EventPriority bukkitPriority;
    private BukkitEventPriority(EventPriority apiPriority, org.bukkit.event.EventPriority bukkitPriority) {
        this.apiPriority = apiPriority;
        this.bukkitPriority = bukkitPriority;
    }
    
    public EventPriority getPriority() { return apiPriority; }
    public org.bukkit.event.EventPriority getBukkitPriority() { return bukkitPriority; }

    /**
     * Convert API Priority setting into Bukkit priority.
     * 
     * @param priority
     * @return
     */
    public static org.bukkit.event.EventPriority convertApiToBukkit(EventPriority priority) {
        return priorityMap.get(priority);
    }
}
