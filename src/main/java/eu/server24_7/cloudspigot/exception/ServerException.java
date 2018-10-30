package eu.server24_7.cloudspigot.exception;

/**
 * Wrapper exception for all exceptions that are thrown by the server.
 */
@SuppressWarnings("serial")
public class ServerException extends Exception {

	public ServerException(String message) {
		super(message);
	}

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerException(Throwable cause) {
		super(cause);
	}

	protected ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
