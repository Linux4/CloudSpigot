package eu.minewars.cloudspigot.event.permission;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.ServerOperator;

import org.bukkit.permissions.Permissible;

/**
 * 
 * Called whenever a {@link Permissible}'s hasPermission() method is called
 *
 */
public class PermissionCheckEvent extends Event {

	private PermissibleBase permissible;
	private ServerOperator opable;
	private String permission;
	private boolean hasPermission;

	/**
	 * 
	 * @param permissible   The {@link PermissibleBase} of which the permission is
	 *                      checked
	 * @param opable        The {@link ServerOperator} of the
	 *                      {@link PermissibleBase}
	 * @param permission    The permission which is checked
	 * @param hasPermission True if the {@link PermissibleBase} has the checked
	 *                      permission
	 */
	public PermissionCheckEvent(PermissibleBase permissible, ServerOperator opable, String permission,
			boolean hasPermission) {
		this.permissible = permissible;
		this.opable = opable;
		this.permission = permission;
		this.hasPermission = hasPermission;
	}

	/**
	 * 
	 * @returnThe {@link PermissibleBase} of which the permission is checked
	 */
	public PermissibleBase getPermissible() {
		return permissible;
	}

	/**
	 * 
	 * @return The {@link ServerOperator} of the {@link PermissibleBase}
	 */
	public ServerOperator getOperator() {
		return opable;
	}

	/**
	 * 
	 * @return The permission which is checked
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * 
	 * @return True if the {@link PermissibleBase} has the checked permission
	 */
	public boolean getHasPermission() {
		return hasPermission;
	}

	/**
	 * 
	 * @param hasPermission Sets if the {@link PermissibleBase} has the checked
	 *                      permission
	 */
	public void setHasPermission(boolean hasPermission) {
		this.hasPermission = hasPermission;
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
