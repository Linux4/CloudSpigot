package eu.server24_7.cloudspigot.exception;

import org.bukkit.plugin.Plugin;

/**
 * Thrown whenever there is an exception with any enabling or disabling of
 * plugins.
 */
@SuppressWarnings("serial")
public class ServerPluginEnableDisableException extends ServerPluginException {
	public ServerPluginEnableDisableException(String message, Throwable cause, Plugin responsiblePlugin) {
		super(message, cause, responsiblePlugin);
	}

	public ServerPluginEnableDisableException(Throwable cause, Plugin responsiblePlugin) {
		super(cause, responsiblePlugin);
	}

	protected ServerPluginEnableDisableException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace, Plugin responsiblePlugin) {
		super(message, cause, enableSuppression, writableStackTrace, responsiblePlugin);
	}
}
