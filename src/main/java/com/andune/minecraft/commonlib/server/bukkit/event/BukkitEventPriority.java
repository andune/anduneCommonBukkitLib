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
