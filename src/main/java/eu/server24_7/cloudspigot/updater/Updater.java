package eu.server24_7.cloudspigot.updater;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.VersionCommand;

public class Updater {

	private static final String BASE_URL = "https://ci.server24-7.eu/job/CloudSpigot/%b%/";
	private static final String URL = BASE_URL + "%t%/cloudspigot-1.8.10-R0.1-SNAPSHOT.jar";

	public static StatusInfo checkUpdate() {
		String version = Bukkit.getVersion();
		if (version == null)
			version = "Custom";
		if (version.startsWith("git-CloudSpigot-")) {
			String[] parts = version.substring("git-CloudSpigot-".length()).split("[-\\s]");
			int distance = VersionCommand.getDistance(null, parts[0]);
			switch (distance) {
			case -1:
				return StatusInfo.ERROR;
			case 0:
				return StatusInfo.LATEST;
			case -2:
				return StatusInfo.LATEST;
			default:
				return new StatusInfo(Status.UPDATE_AVAILABLE, distance);
			}
		} else {
			return StatusInfo.ERROR;
		}
	}

	public enum Status {
		ERROR, LATEST_VERSION, UNKNOWN_VERSION, UPDATE_AVAILABLE;
	}

	public static class StatusInfo {
		public static final StatusInfo ERROR = new StatusInfo(Status.ERROR, -1);
		public static final StatusInfo UNKNOWN = new StatusInfo(Status.UNKNOWN_VERSION, -1);
		public static final StatusInfo LATEST = new StatusInfo(Status.LATEST_VERSION, 0);
		private Status s;
		private int c;

		public StatusInfo(Status s, int c) {
			this.s = s;
			this.c = c;
		}

		public Status getStatus() {
			return s;
		}

		public int getVersionsBehind() {
			return c;
		}
	}

	private static boolean isJava8() {
		return getJavaVersion() == 1.8;
	}

	private static boolean isJava9Up() {
		return getJavaVersion() > 1.8;
	}

	private static double getJavaVersion() {
		String version = System.getProperty("java.version");
		int pos = version.indexOf('.');
		pos = version.indexOf('.', pos + 1);
		return Double.parseDouble(version.substring(0, pos));
	}

	private static long getNewestBuild() {
		long b = 0;
		while (true) {
			b++;
			try {
				URL url = new URL(BASE_URL.replaceFirst("%b%", "" + b));
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				if (conn.getResponseCode() != 200) {
					b--;
					break;
				}
			} catch (IOException e) {
				break;
			}
		}
		return b;
	}

	public static void update() {

	}

}
