package eu.server24_7.cloudspigot.exception;

import org.bukkit.Bukkit;
//import org.bukkit.entity.ThrownExpBottle; // CloudSpigot
import eu.server24_7.cloudspigot.event.ServerExceptionEvent;

/**
 * Thrown when the internal server throws a recoverable exception.
 */
@SuppressWarnings("serial")
public class ServerInternalException extends ServerException {

    public ServerInternalException(String message) {
        super(message);
    }

    public ServerInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerInternalException(Throwable cause) {
        super(cause);
    }

    protected ServerInternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void reportInternalException(Throwable cause) {
        try {
            Bukkit.getPluginManager().callEvent(new ServerExceptionEvent(new ServerInternalException(cause)));
        } catch (Throwable t) {
            t.printStackTrace(); // Don't want to rethrow!
        }
    }
}
