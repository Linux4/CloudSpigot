package eu.server24_7.cloudspigot.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerAutoRespawnEvent extends Event {

	private Player p;
	private Location deathLoc;
	private Location respawnLoc;

	public PlayerAutoRespawnEvent(Player p, Location deathLoc, Location respawnLoc) {
		this.p = p;
		this.deathLoc = deathLoc;
		this.respawnLoc = respawnLoc;
	}

	public Player getPlayer() {
		return p;
	}

	public Location getDeathLocation() {
		return deathLoc;
	}

	public Location getRespawnLocation() {
		return respawnLoc;
	}

	public DamageCause getDeathCause() {
		return p.getLastDamageCause().getCause();
	}

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

	public Player getKiller() {
		return p.getKiller();
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}

