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
