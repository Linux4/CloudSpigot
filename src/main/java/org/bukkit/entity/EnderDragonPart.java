package org.bukkit.entity;

/**
 * Represents an ender dragon part
 */
public interface EnderDragonPart extends ComplexEntityPart, Damageable {
	@Override
	public EnderDragon getParent();
}
