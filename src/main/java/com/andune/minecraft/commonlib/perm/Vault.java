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
        return "vault";
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
        return "VAULT:"+permName;
    }
}
