/**
 * 
 */
package com.andune.minecraft.commonlib.perm;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;

import com.andune.minecraft.commonlib.Logger;
import com.andune.minecraft.commonlib.LoggerFactory;

/**
 * @author andune
 *
 */
public abstract class BasePermissionChecks implements PermissionInterface {
    private final Logger log = LoggerFactory.getLogger(BasePermissionChecks.class);

    protected Plugin plugin;

    /*
     * Base checks consistent across permissions plugins; console and ops
     * always have access.
     */
    @Override
    public boolean has(Permissible sender, String permission) {
        Player p = null;
        // console always has access
        if( sender instanceof ConsoleCommandSender ) {
            log.debug("has() ConsoleCommandSender=true");
            return true;
        }
        if( sender instanceof Player )
            p = (Player) sender;
        
        log.debug("has() p={}", p);
        
        if( p == null ) {
            log.debug("has() p=null, returning false");
            return false;
        }
        else if( p.isOp() ) {  // legacy op check, op always has access
            log.debug("has() p.isOp=true, returning true");
            return true;
        }
        
        // no special op/console access granted, must run player check
        return false;
    }

    /* 
     * If using string-based player names and no specific world, we assume
     * default of "world".
     */
    @Override
    public boolean has(String player, String permission) {
        return has("world", player, permission);
    }
    
    /**
     * Determine if a given player name is an operator.
     * 
     * @param player
     * @return
     */
    protected boolean isOp(String player) {
        Player p = plugin.getServer().getPlayer(player);
        if( p != null )
            return p.isOp();
        else
            return false;
    }
}
