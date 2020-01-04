package eu.server24_7.cloudspigot.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * 
 * Called when a {@link Player} was autorespawned
 *
 */
public class PlayerAutoRespawnEvent extends Event {

	private Player p;
	private Location deathLoc;
	private Location respawnLoc;

	/**
	 * 
	 * @param p          The {@link Player} which was autorespawned
	 * @param deathLoc   The {@link Location} where the {@link Player} died
	 * @param respawnLoc The {@link Location} where the {@link Player} autorespawned
	 */
	public PlayerAutoRespawnEvent(Player p, Location deathLoc, Location respawnLoc) {
		this.p = p;
		this.deathLoc = deathLoc;
		this.respawnLoc = respawnLoc;
	}

	/**
	 * 
	 * @return The {@link Player} which was autorespawned
	 */
	public Player getPlayer() {
		return p;
	}

	/**
	 * 
	 * @return The {@link Location} where the {@link Player} died
	 */
	public Location getDeathLocation() {
		return deathLoc;
	}

	/**
	 * 
	 * @return The {@link Location} where the {@link Player} autorespawned
	 */
	public Location getRespawnLocation() {
		return respawnLoc;
	}

	/**
	 * 
	 * @return The DeathCause of the {@link Player}
	 */
	public DamageCause getDeathCause() {
		return p.getLastDamageCause().getCause();
	}

	/**
	 * 
	 * @return True if the {@link Player} was killed by another {@link Player}
	 */
	public boolean killedByPlayer() {
		if (p.getLastDamageCause().getEntity() instanceof Player)
			return true;
		if (p.getLastDamageCause().getEntity() instanceof Projectile) {
			Projectile a = (Projectile) p.getLastDamageCause().getEntity();
			if (a.getShooter() instanceof Player)
				return true;
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @return The killer of the {@link Player}. Returns null if not killed by a
	 *         {@link Player}
	 */
	public Player getKiller() {
		return p.getKiller();
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
