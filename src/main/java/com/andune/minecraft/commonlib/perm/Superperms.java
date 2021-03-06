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

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import com.andune.minecraft.commonlib.Logger;
import com.andune.minecraft.commonlib.LoggerFactory;

/**
 * @author andune
 *
 */
public class Superperms extends BasePermissionChecks implements PermissionInterface {
    // for superperms, groups are permissions that start with a group prefix
    private static final String GROUP_PREFIX = "group.";
    private final Logger log = LoggerFactory.getLogger(Superperms.class);

    @Override
    public String getSystemName() {
        return "SUPERPERMS";
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#init(org.bukkit.plugin.Plugin)
     */
    @Override
    public boolean init(Plugin plugin) {
        super.plugin = plugin;
        return true;
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#has(org.bukkit.permissions.Permissible, java.lang.String)
     */
    @Override
    public boolean has(Permissible sender, String permission) {
        boolean permAllowed = super.has(sender, permission);
        if( !permAllowed && sender instanceof Player )
            permAllowed = sender.hasPermission(permission);
        log.debug("has() permAllowed={}", permAllowed);
        return permAllowed;
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#has(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean has(String world, String player, String permission) {
        boolean permAllowed = false;
        
        Player p = plugin.getServer().getPlayer(player);
        // technically this is not guaranteed to be accurate since superperms
        // doesn't support checking cross-world perms. Upgrade to a better
        // perm system if you care about this.
        if( p != null )
            permAllowed = p.hasPermission(permission);
        
        return permAllowed || isOp(player);
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#getPlayerGroup(java.lang.String, java.lang.String)
     */
    @Override
    public String getPlayerGroup(String world, String playerName) {
        Player player = plugin.getServer().getPlayer(playerName);
        if( player != null )
            return getSuperpermsGroup(player);
        else
            return null;
    }

    /**
     * Superperms has no group support, but we fake it (this is slow and stupid
     * since it has to iterate through ALL permissions a player has). But if
     * you're attached to superperms and not using a nice plugin like bPerms
     * and Vault then this is as good as it gets.
     * 
     * @param player
     * @return the group name or null
     */
    private String getSuperpermsGroup(Player player) {
        if( player == null )
            return null;
        
        String group = null;
        
        // this code shamelessly adapted from WorldEdit's WEPIF support for superperms
        Permissible perms = getPermissible(player);
        if (perms != null) {
            for (PermissionAttachmentInfo permAttach : perms.getEffectivePermissions()) {
                String perm = permAttach.getPermission();
                if (!(perm.startsWith(GROUP_PREFIX) && permAttach.getValue())) {
                    continue;
                }
                
                // we just grab the first "group.X" permission we can find
                group = perm.substring(GROUP_PREFIX.length(), perm.length());
                break;
            }
        }
        
        return group;
    }

    /**
     * This code shamelessly stolen from WEPIF in order to support a fake
     * "group" notion for Superperms.
     * 
     * @param offline
     * @return
     */
    private Permissible getPermissible(OfflinePlayer offline) {
        if (offline == null) return null;
        Permissible perm = null;
        if (offline instanceof Permissible) {
            perm = (Permissible) offline;
        } else {
            Player player = offline.getPlayer();
            if (player != null) perm = player;
        }
        return perm;
    }

    public String getSystemInUseString() {
        return getSystemName();
    }
}
