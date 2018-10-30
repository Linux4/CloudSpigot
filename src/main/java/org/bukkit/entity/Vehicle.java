package org.bukkit.entity;

import org.bukkit.util.Vector;

/**
 * Represents a vehicle entity.
 */
public interface Vehicle extends Entity {

	/**
	 * Gets the vehicle's velocity.
	 *
	 * @return velocity vector
	 */
	@Override
	public Vector getVelocity();

	/**
	 * Sets the vehicle's velocity.
	 *
	 * @param vel velocity vector
	 */
	@Override
	public void setVelocity(Vector vel);
}
