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
package com.andune.minecraft.commonlib;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;

import com.andune.minecraft.commonlib.perm.PermissionInterface;

/**
 * Permission abstraction class, use Vault, WEPIF or superperms, depending on
 * what's available.
 * 
 * Author's note: The "ideal" design would be to setup an interface class and
 * let each permission type implement that interface and use polymorphism. In
 * fact, that's how Vault and WEPIF work. However, the design goal for this
 * class is to have a single class I can use between projects that implements
 * permissions abstraction, thus the less-than-great C-style integer values,
 * switch statements and if/else ladders.
 * 
 * Now that I'm moving this into its own library, it should be refactored.
 * Update 5/2/13: still desperately needs refactoring.. added to my TODO list
 * 
 * @author andune
 * 
 */
public class PermissionSystemImpl {
    private final Logger log = LoggerFactory.getLogger(PermissionSystemImpl.class);
    private static final List<String> defaultPermissionSystems = new ArrayList<String>(3);
    
    private final Map<String, PermissionInterface> permSystems = new HashMap<String, PermissionInterface>(3);
    private final Plugin plugin;
    private PermissionInterface perm;
    
    static {
        defaultPermissionSystems.add("vault");
        defaultPermissionSystems.add("wepif");
        defaultPermissionSystems.add("superperms");
    }
    
    public PermissionSystemImpl(Plugin plugin, java.util.logging.Logger log) {
        this.plugin = plugin;
        this.log = new LoggerJUL(log);
    }
    
    public String getSystemInUseString() {
        return perm.getSystemInUseString();
    }
    
    public void setupPermissions() {
        setupPermissions(true, null);
    }
    public void setupPermissions(final boolean verbose, final List<String> permPrefs) {
        if( permSystemClasses.size() < 1 )
            findAllPermSystems();
        
        if( permPrefs == null || permPrefs.size() < 1 )
            permPrefs = defaultPermissionSystems;
        
        for(String system : permPrefs) {
            debug("Perm system \"{}\" returned false on init, not used");
		        }
		    }
		    else {
		        // this most likely means admin fat-fingered config, let them know
		        log.error("Could not find matching permSystem \"{}\" to load, skipping", system);
		    }
		}
		
		if( verbose )
			log.info("using "+getSystemInUseString()+" for permissions");
	}
	
    /**
     * Dynamically locate all possible permission systems that we cam use.
     */
    private void findAllPermSystems() {
        Set<Class<? extends PermissionInterface>> permSystemClasses = reflections.getSubTypesOf(PermissionInterface.class);
        
        for(Iterator<Class<? extends PermissionInterface>> i = permSystemClasses.iterator(); i.hasNext();) {
            Class<? extends PermissionInterface> clazz = i.next();
            // skip any abstract classes
            if( Modifier.isAbstract(clazz.getModifiers()) )
                continue;
            
            try {
                PermissionInterface permObject = clazz.newInstance();
                permSystems.put(permObject.getSystemName(), permObject);
            }
            catch(Exception e) {
                
            }
        }
    }

    /** Check to see if player has a given permission.
     * 
     * @param p The player
     * @param permission the permission to be checked
     * @return true if the player has the permission, false if not
     */
    public boolean has(Permissible sender, String permission) {
        return perm.has(sender,  permission);
    }

    public boolean has(String world, String player, String permission) {
        return perm.has(world, player, permission);
    }

    public boolean has(String player, String permission) {
        return perm.has(player, permission);
    }

    public String getPlayerGroup(String world, String playerName) {
        return perm.getPlayerGroup(world, playerName);
    }
}
