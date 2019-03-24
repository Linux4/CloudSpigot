package eu.server24_7.cloudspigot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * EULA class which automatically accepts the EULA
 *
 */
public class CloudSpigotEULA {

	private static final Logger a = LogManager.getLogger();
	private final File b;
	private final boolean c;

	public CloudSpigotEULA(File file1) {
		b = file1;
		c = this.a(file1);
	}

	private boolean a(File file1) {
		FileInputStream fileinputstream = null;
		boolean flag = false;

		try {
			final Properties properties = new Properties();

			fileinputstream = new FileInputStream(file1);
			properties.load(fileinputstream);
			flag = Boolean.parseBoolean(properties.getProperty("eula", "false"));
		} catch (final Exception exception) {
			flag = true;
			b();
		} finally {
			try {
				fileinputstream.close();
			} catch (final Exception e) {
				;
			}
		}

		return flag;
	}

	public boolean a() {
		return c;
	}

	public void b() {
		FileOutputStream fileoutputstream = null;

		try {
			final Properties properties = new Properties();

			fileoutputstream = new FileOutputStream(b);
			properties.setProperty("eula", "true");
			properties.store(fileoutputstream,
					"By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
		} catch (final Exception exception) {
			CloudSpigotEULA.a.warn("Failed to save " + b, exception);
		} finally {
			try {
				fileoutputstream.close();
			} catch (final IOException e) {
				;
			}
		}
	}
}
