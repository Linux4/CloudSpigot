package org.spigotmc;

public class SneakyThrow {

	public static void sneaky(Throwable t) {
		throw SneakyThrow.<RuntimeException>superSneaky(t);
	}

	@SuppressWarnings("unchecked")
	private static <T extends Throwable> T superSneaky(Throwable t) throws T {
		throw (T) t;
	}
}
