package eu.server24_7.cloudspigot.event.permission;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.ServerOperator;

public class PermissionCheckEvent extends Event {

	private PermissibleBase permissible;
	private ServerOperator opable;
	private String permission;
	private boolean hasPermission;

	public PermissionCheckEvent(PermissibleBase permissible, ServerOperator opable, String permission, boolean hasPermission) {
		this.permissible = permissible;
		this.opable = opable;
		this.permission = permission;
		this.hasPermission = hasPermission;
	}

	public PermissibleBase getPermissible() {
		return permissible;
	}

	public ServerOperator getOperator() {
		return opable;
	}

	public String getPermission() {
		return permission;
	}

	public boolean getHasPermission() {
		return hasPermission;
	}

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
