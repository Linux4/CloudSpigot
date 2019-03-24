package eu.server24_7.cloudspigot.updater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.VersionCommand;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.base.Charsets;

public class Updater {

	private static final String BASE_URL = "https://ci.server24-7.eu/job/CloudSpigot/latest/";
	private static final String URL = BASE_URL + "%t%/cloudspigot-1.8.10-R0.1-SNAPSHOT.jar";
	private static final String GIT_URL = BASE_URL + ".gitrev";

	public static Status checkUpdate() {
		String version = Bukkit.getVersion();
		if (version == null)
			version = "Custom";
		if (version.startsWith("git-CloudSpigot-")) {
			String[] parts = version.substring("git-CloudSpigot-".length()).split("[-\\s]");
			int distance = VersionCommand.getDistance(null, parts[0]);
			switch (distance) {
			case -1:
				return Status.ERROR;
			case 0:
				return Status.LATEST_VERSION;
			case -2:
				return Status.LATEST_VERSION;
			default:
				return Status.UPDATE_AVAILABLE;
			}
		} else {
			return Status.ERROR;
		}
	}

	public enum Status {
		ERROR, LATEST_VERSION, UNKNOWN_VERSION, UPDATE_AVAILABLE;
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

	public static boolean update() {
		if (newestAvailable()) {
			try {
				URL url = new URL(URL.replaceFirst("%t%", "java8"));
				if (isJava9Up()) {
					url = new URL(URL.replaceFirst("%t%", "java11"));
				} else {
					if (!isJava8()) {
						throw new IllegalStateException("Unsupported Java version: " + getJavaVersion());
					}
				}

				File jar = new File(Updater.class.getProtectionDomain().getCodeSource().getLocation().getPath());
				if (jar.isDirectory()) {
					throw new IllegalStateException("Not running from JAR file!");
				} else {
					try {
						download(url, jar);
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	private static void download(URL url, File f) throws IOException {
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		FileOutputStream fileOutputStream = new FileOutputStream(f);
		byte dataBuffer[] = new byte[1024];
		int bytesRead;
		while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
			fileOutputStream.write(dataBuffer, 0, bytesRead);
		}
		fileOutputStream.close();
	}

	private static String getNewestCommit() {
		try {
			HttpsURLConnection connection = (HttpsURLConnection) new URL(
					"https://api.github.com/repos/Server24-7/CloudSpigot/commits/master").openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), Charsets.UTF_8));
			JSONObject obj = (JSONObject) new JSONParser().parse(reader);
			return (String) obj.get("sha");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	private static boolean newestAvailable() {
		String hash = getNewestCommit();
		try {
			HttpsURLConnection connection = (HttpsURLConnection) new URL(GIT_URL).openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			return hash.equalsIgnoreCase(reader.readLine());
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {

	}

}
