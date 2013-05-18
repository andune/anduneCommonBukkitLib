/**
 * 
 */
package com.andune.minecraft.commonlib.perm;

import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;

/**
 * General interface used to access an implementing permission system.
 * 
 * @author andune
 *
 */
public interface PermissionInterface {
    /**
     * Every permission system must have a name, which is used by admins to
     * control permission system load order in config files.
     * 
     * @return
     */
    public String getSystemName();
    
    /**
     * Initialize this permission system.
     * 
     * @param plugin
     * @return true if permission plugin exists and initialization succeeds,
     *         false if not
     */
    public boolean init(Plugin plugin);
    
    /** Check to see if player has a given permission.
     * 
     * @param p The player
     * @param permission the permission to be checked
     * @return true if the player has the permission, false if not
     */
    public boolean has(Permissible sender, String permission);
    public boolean has(String world, String player, String permission);
    public boolean has(String player, String permission);
    public String getPlayerGroup(String world, String playerName);
    
    /**
     * Return details about the permission system in use.
     * 
     * @return
     */
    public String getSystemInUseString();
}
