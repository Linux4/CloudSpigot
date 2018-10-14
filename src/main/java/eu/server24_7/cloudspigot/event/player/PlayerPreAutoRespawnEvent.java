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

public class PlayerPreAutoRespawnEvent extends Event implements Cancellable {

	private Player p;
	private Location deathLoc;
	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();

	public PlayerPreAutoRespawnEvent(Player p, Location deathLoc) {
		this.p = p;
		this.deathLoc = deathLoc;
	}

	public Player getPlayer() {
		return p;
	}

	public Location getDeathLocation() {
		return deathLoc;
	}

	public DamageCause getDeathCause() {
		return p.getLastDamageCause().getCause();
	}

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

	public Player getKiller() {
		return p.getKiller();
	}

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

