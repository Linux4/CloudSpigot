package eu.server24_7.cloudspigot.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * 
 * Called before a {@link Player} autorespawns
 *
 */
public class PlayerPreAutoRespawnEvent extends Event implements Cancellable {

	private Player p;
	private Location deathLoc;
	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();

	/**
	 * 
	 * @param p        The {@link Player} autorespawning
	 * @param deathLoc The {@link Location} where the {@link Player} died
	 */
	public PlayerPreAutoRespawnEvent(Player p, Location deathLoc) {
		this.p = p;
		this.deathLoc = deathLoc;
	}

	/**
	 * 
	 * @return The {@link Player} autorespawning
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
		if (p.getLastDamageCause().getEntity() instanceof Arrow)
			return ((Arrow) p.getLastDamageCause().getEntity()).getShooter() instanceof Player;
		if (p.getLastDamageCause().getEntity() instanceof Snowball)
			return ((Snowball) p.getLastDamageCause().getEntity()).getShooter() instanceof Player;
		if (p.getLastDamageCause().getEntity() instanceof Egg)
			return ((Egg) p.getLastDamageCause().getEntity()).getShooter() instanceof Player;
		return false;
	}

	/**
	 * 
	 * @return The killer of the {@link Player}. Returns null if not killed by a
	 *         {@link Player}
	 * 
	 */
	public Player getKiller() {
		return p.getKiller();
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}
}
