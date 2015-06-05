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
package com.andune.minecraft.commonlib;

import javax.inject.Inject;
import java.util.EnumSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/** General purpose teleportation routines.
 * 
 * @author morganm
 *
 */
public class Teleport {
	// maximum number of loops findSafeLocation is allowed to do under
	// any circumstance. Infinite recursion protection.
	private static final int MAX_LOOPS = 50;

	public static final int FLAG_NO_WATER = 0x01; 
	public static final int FLAG_NO_LILY_PAD = 0x02; 
	public static final int FLAG_NO_LEAVES = 0x04; 
	public static final int FLAG_NO_ICE = 0x08;

    private static EnumSet<Material> safeMaterials = EnumSet.noneOf(Material.class);
	
	/*
	private final static BlockFace[] directions = new BlockFace[] {
		BlockFace.UP,
		BlockFace.NORTH,
		BlockFace.WEST,
		BlockFace.SOUTH,
		BlockFace.EAST,
		BlockFace.DOWN
	};
	*/
	
	private final Logger log ;
	private String currentTeleporter;
	
	static {
        safeMaterials.add(Material.AIR);
        safeMaterials.add(Material.YELLOW_FLOWER);
        safeMaterials.add(Material.RED_ROSE);
        safeMaterials.add(Material.BROWN_MUSHROOM);
        safeMaterials.add(Material.RED_MUSHROOM);
        safeMaterials.add(Material.LONG_GRASS);
        safeMaterials.add(Material.DEAD_BUSH);
        safeMaterials.add(Material.CROPS);
        safeMaterials.add(Material.TORCH);
        safeMaterials.add(Material.REDSTONE_WIRE);
        safeMaterials.add(Material.REDSTONE_TORCH_ON);
        safeMaterials.add(Material.REDSTONE_TORCH_OFF);
        safeMaterials.add(Material.REDSTONE_LAMP_OFF);
        safeMaterials.add(Material.REDSTONE_LAMP_ON);
        safeMaterials.add(Material.WOOD_PLATE);
        safeMaterials.add(Material.DIODE_BLOCK_ON);
        safeMaterials.add(Material.DIODE_BLOCK_OFF);
        safeMaterials.add(Material.POWERED_RAIL);
        safeMaterials.add(Material.DETECTOR_RAIL);
        safeMaterials.add(Material.RAILS);
        safeMaterials.add(Material.SIGN_POST);
        safeMaterials.add(Material.WALL_SIGN);

        safeMaterials.add(Material.CARPET);
        safeMaterials.add(Material.STONE_PLATE);
        safeMaterials.add(Material.WOOD_PLATE);
        safeMaterials.add(Material.GOLD_PLATE);
        safeMaterials.add(Material.IRON_PLATE);
        safeMaterials.add(Material.LADDER);
        safeMaterials.add(Material.SNOW);
        safeMaterials.add(Material.WEB);
        safeMaterials.add(Material.VINE);
        safeMaterials.add(Material.SAPLING);
        safeMaterials.add(Material.STONE_BUTTON);
        safeMaterials.add(Material.LEVER);
        safeMaterials.add(Material.SUGAR_CANE_BLOCK);
        safeMaterials.add(Material.CARROT);
        safeMaterials.add(Material.POTATO);
        safeMaterials.add(Material.WOOD_BUTTON);
	}
	
	@Inject
	private Teleport(Logger log) {
		this.log = log;
	}
	
	public void setCurrentTeleporter(String name) {
		this.currentTeleporter = name;
	}
	public String getCurrentTeleporter() { return currentTeleporter; }
	
	public boolean isSafeBlock(Block b, int flags) {
		final Block up = b.getRelative(BlockFace.UP);
		final Block down = b.getRelative(BlockFace.DOWN);
        final Material bMaterial = b.getType();
        final Material upMaterial = up.getType();
        final Material downMaterial = down.getType();

        if( safeMaterials.contains(bMaterial) && safeMaterials.contains(upMaterial) // block & UP are safe?
                && (downMaterial != Material.LAVA && down.getType() != Material.STATIONARY_LAVA) // no lava underneath
                && (downMaterial != Material.AIR) // no air underneath
                && (downMaterial != Material.FIRE) // not fire below
                && (downMaterial != Material.VINE) // not vine below, results in possible unsafe teleports (over lava, water, etc)
                )
		{
			// check for water
			if( (flags & FLAG_NO_WATER) > 0 ) {
                if( bMaterial == Material.WATER
                        || bMaterial == Material.STATIONARY_WATER
                        || downMaterial == Material.WATER
                        || downMaterial == Material.STATIONARY_WATER )
				{
					return false;
				}
			}
			
			// check for lily pads
            if( (flags & FLAG_NO_LILY_PAD) > 0 && downMaterial == Material.WATER_LILY ) {
				return false;
			}
			
			// check for leaves
			if( (flags & FLAG_NO_LEAVES) > 0 ) {
                if( downMaterial == Material.LEAVES )
					return false;
				
				// we check 2 blocks down for FLAG_NO_LEAVES, because often snow
				// is on top of leaves, so we need to check 1 block lower
				final Block down2 = down.getRelative(BlockFace.DOWN);
				if( down2.getTypeId() == Material.LEAVES.getId() )
					return false;
			}
			
			// check for ice
            if( (flags & FLAG_NO_ICE) > 0 && downMaterial == Material.ICE ) {
				return false;
			}

			log.debug("isSafeBlock() block is safe b={}, up={}, down={}", b, up, down);
			
			// if we make it here, we've made it through all hazardous checks
			// so the block is considered safe to teleport to
			return true;
		}
		else
			return false;
		
	}
	
    /**
     * This is a recursive routine that checks concentric cubes out from
     * the original block that is passed in. This has the effect that this
     * algorithm basically looks for the "nearest" safe block to the
     * original. 
     * 
     * @param baseLocation
     * @param level
     * @param bounds
     * @param flags
     * @return
     */
	private Location findSafeLocation2(final Location baseLocation, final int level,
			final Bounds bounds, final int flags, int loops)
	{
		if( loops > MAX_LOOPS ) {
			log.warn("findSafeLocation fail-safe triggered. Aborting to prevent infinite recursion. Location={}, bounds={}, flags={}",
					baseLocation, bounds, flags);
			return baseLocation;
		}

		log.debug("findSafeLocation2(): level={}, baseLocation={}, flags={}",
		        level, baseLocation, flags);
		final World w = baseLocation.getWorld();
        if( w == null ) {
            log.info("Warning: location refers to non-existant world: "+baseLocation);
            return null;
        }

		final int baseX = baseLocation.getBlockX();
		final int baseY = baseLocation.getBlockY();
		final int baseZ = baseLocation.getBlockZ();
		
		int minX = baseX - level;
		int maxX = baseX + level;
		int minY = baseY - level;
		int maxY = baseY + level;
		int minZ = baseZ - level;
		int maxZ = baseZ + level;
		
		if( minY < bounds.minY )
			minY = bounds.minY;
		if( maxY > bounds.maxY )
			maxY = bounds.maxY;
		
		log.debug("findSafeLocation2(): bounds.maxY={}, bounds.minY={}", bounds.maxY, bounds.minY);
		log.debug("findSafeLocation2(): maxY={}, minY={}", maxY, minY);
		
		long startTime = System.currentTimeMillis();
		int checkedBlocks=0;
        // if this is our first time through the loop, we check the block
        // itself and one above. This has the effect of making spawns appear
        // correct when they are set on half-slabs.
        if( level == 0 ) {
            Block baseBlock = w.getBlockAt(baseLocation);
            if( isSafeBlock(baseBlock, flags) ) {
                log.debug("findSafeLocation2(): found safe block (original location) {}", baseBlock);
                return baseLocation;
            }
            else {
                Block upBlock = baseBlock.getRelative(BlockFace.UP);
                if( isSafeBlock(upBlock, flags) ) {
                    log.debug("findSafeLocation2(): found safe block (UP block) {}", upBlock);
                    Location l = upBlock.getLocation();
                    // maintain original pitch/yaw
                    l.setPitch(baseLocation.getPitch());
                    l.setYaw(baseLocation.getYaw());
                    return l;
                }
            }
        }
        // otherwise we loop through the current cocentric cube
        else {
            for(int x = maxX; x >= minX; x--) {
                for(int y=maxY; y >= minY; y--) {
                    for(int z=maxZ; z >= minZ; z--) {
                        // we only check the level that we're at, at least one
                        // of the axis must be at the current level
                        if( x != maxX && x != minX
                                && y != maxY && y != minY 
                                && z != maxZ && z != minZ )
                            continue;

                        Block b = w.getBlockAt(x, y, z);
                        if( isSafeBlock(b, flags) ) {
                            log.debug("findSafeLocation2(): found safe block {}",b);
                            return b.getLocation();
                        }
                        checkedBlocks++;
                    }
                }
            }
        }
		
		long totalTime = System.currentTimeMillis() - startTime;
		log.debug("findSafeLocation2(): no safe location found at level {}, checked {} total blocks. Recursing to next level. (total time = {})",
		        level, checkedBlocks, totalTime);
		
		// we only recurse so far before we give up
		if( level+1 > bounds.maxRange ) {
			// check the highest Block at the given X/Z; if it's higher than the maxY
			// we've checked, then try there. This has the effect that if we try to
			// spawn deep into solid blocks and can't find a safe place, we'll spawn
			// at the top Y, much like the vanilla MC algorithm.
			// also note we check if it's lower than minY, because it's possible we
			// originally received a teleport request high up in the middle of the
			// sky, so we'll want to try again at the highest block level.
			// the mixY/minY checks prevent this from being infinitely recursive,
			// since this can only be true once.
	    	Location highest = w.getHighestBlockAt(baseLocation).getLocation();

			// check for the situation where there are no blocks at all - as in
			// an intentionally empty world. This is manifest by the highest
			// block being reported as less than Y=1. In this situation, there
			// is no safe place to be found, so exit here to avoid an infinite
			// loop.
			if (highest.getBlockY() < 1) {
				log.debug("findSafeLocation2(): hit maximum recursion distance {}, highest Y-block is {}. Aborting safe algorithm",
						bounds.maxRange, highest.getY());
				return null;
			}

	    	if( highest.getY() > maxY || highest.getY() < minY ) {
				log.debug("findSafeLocation2(): hit maximum recursion distance {}, moving to highest Y-block at {} and trying again",
				        bounds.maxRange, highest.getY());
				return findSafeLocation2(highest, 0, bounds, flags, loops+1);
	    	}
			log.debug("findSafeLocation2(): hit maximum recursion distance {}, returning null", bounds.maxRange);
			return null;
		}
		
		return findSafeLocation2(baseLocation, level+1, bounds, flags, loops+1);
	}
	
	/** Safely teleport a player to a location. Should avoid them being stuck in blocks,
	 * teleported over lava, etc.
	 * 
	 * @param p
	 * @param l
	 */
	public void safeTeleport(final Player p, final Location l, final TeleportCause cause) {
		Location safeLocation = safeLocation(l);
		if( safeLocation != null ) {
			setCurrentTeleporter(p.getName());
			p.teleport(safeLocation, cause);
			setCurrentTeleporter(null);
		}
		else
			log.info("safeTeleport: couldn't find safe location near location "+l);
	}

	/** Given a location, find the nearest "safe" location, ie. that won't suffocate a
	 * player, spawn them over lava, etc.
	 * 
	 * @param l the location to start searching from
	 * @param bounds Bounds specifying limits on the safeTeleport algorithm
	 * @param flags flags that control the safeTeleport algorithm, see TeleportOptions
	 * @return the safe location or null if none could be found
	 */
	public Location safeLocation(Location l, Bounds bounds, int flags) {
		if( bounds == null )
			bounds = defaultBounds;
		
		Location target = findSafeLocation2(l, 0, bounds, flags, 0);
		
		if( target != null ) {
			if( !target.equals(l) ) {
				// preserve pitch/yaw
				target.setPitch(l.getPitch());
				target.setYaw(l.getYaw());

				// adjust by 0.5 so we teleport to the middle of the block, not
				// the edge
				target.setX(target.getX() + 0.5);
				target.setZ(target.getZ() + 0.5);
				log.debug("adjusted coordinates to middle. x={}, z={}", target.getX(), target.getZ());
			}
			else
				log.debug("safeLocation(): original location is safe");
		}
		else
			log.info("safeLocation: couldn't find nearby safe location, using original location "+l);
		
		log.debug("safeLocation(): target=",target);
		return target;
	}
	
	/** Given a location, find the nearest "safe" location, ie. that won't suffocate a
	 * player, spawn them over lava, etc.
	 * 
	 * @param l
	 */
	public Location safeLocation(Location l) {
		return safeLocation(l, defaultBounds, 0);
	}

	public Bounds getDefaultBounds() { return defaultBounds; }

	private final Bounds defaultBounds = new Bounds();
	public static class Bounds {
		// min/max Y bounds, can be used to prevent safe location from being
		// below or above a certain Y bound
		public int minY=0;
		public int maxY=255;
		
		// the range we check from the baseLocation. Checks get exponentially larger
		// as the volume of the cube grows, so don't go too large (max probably
		// 25 or so for reasonable performance).
		public int maxRange=10;
		
		public String toString() {
			return "minY="+minY
				+", maxY="+maxY
				+", maxRange="+maxRange;
		}
	}
}
