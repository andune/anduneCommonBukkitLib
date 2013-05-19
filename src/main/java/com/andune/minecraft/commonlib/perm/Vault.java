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

import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.andune.minecraft.commonlib.Logger;
import com.andune.minecraft.commonlib.LoggerFactory;

/**
 * @author andune
 *
 */
public class Vault extends BasePermissionChecks implements PermissionInterface {
    private final Logger log = LoggerFactory.getLogger(Vault.class);
    private net.milkbowl.vault.permission.Permission vaultPermission = null;

    @Override
    public String getSystemName() {
        return "VAULT";
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#init(org.bukkit.plugin.Plugin)
     */
    @Override
    public boolean init(Plugin plugin) {
        this.plugin = plugin;
        
        Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");
        if( vault != null ) {
            RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if (permissionProvider != null) {
                vaultPermission = permissionProvider.getProvider();
            }
        }
        
        return (vaultPermission != null);
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#has(org.bukkit.permissions.Permissible, java.lang.String)
     */
    @Override
    public boolean has(Permissible sender, String permission) {
        boolean permAllowed = super.has(sender, permission);
        if( !permAllowed && sender instanceof Player )
            permAllowed = vaultPermission.has((Player) sender, permission);
        log.debug("has() permAllowed={}", permAllowed);
        return permAllowed;
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#has(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean has(String world, String player, String permission) {
        return vaultPermission.has(world, player, permission) || isOp(player);
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#getPlayerGroup(java.lang.String, java.lang.String)
     */
    @Override
    public String getPlayerGroup(String world, String playerName) {
        return vaultPermission.getPrimaryGroup(world, playerName);
    }
    
    public String getSystemInUseString() {
        final String permName = plugin.getServer().getServicesManager().getRegistration(Permission.class).getProvider().getName();
        return getSystemName()+":"+permName;
    }
}
