package eu.minewars.cloudspigot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Throwables;

import net.minecraft.server.Items;
import net.minecraft.server.MinecraftServer;

@SuppressWarnings("unused")
public class CloudSpigotConfig {

	private static File CONFIG_FILE;
	private static final String HEADER = "This is the main configuration file for CloudSpigot.\n"
			+ "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
			+ "with caution, and make sure you know what each option does before configuring.\n" + "\n"
			+ "If you need help with the configuration or have any questions related to CloudSpigot,\n"
			+ "join us at Discord.\n" + "\n" + "Discord: https://discord.gg/5qp26hf\n";
	/* ======================================================================== */
	public static YamlConfiguration config;
	static int version;
	static Map<String, Command> commands;
	/* ======================================================================== */

	public static void init(File configFile) {
		CloudSpigotConfig.CONFIG_FILE = configFile;
		CloudSpigotConfig.config = new YamlConfiguration();
		try {
			CloudSpigotConfig.config.load(CloudSpigotConfig.CONFIG_FILE);
		} catch (final IOException ex) {
		} catch (final InvalidConfigurationException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not load paper.yml, please correct your syntax errors", ex);
			throw Throwables.propagate(ex);
		}
		CloudSpigotConfig.config.options().header(CloudSpigotConfig.HEADER);
		CloudSpigotConfig.config.options().copyDefaults(true);

		CloudSpigotConfig.commands = new HashMap<String, Command>();

		CloudSpigotConfig.version = CloudSpigotConfig.getInt("config-version", 9);
		CloudSpigotConfig.set("config-version", 9);
		CloudSpigotConfig.readConfig(CloudSpigotConfig.class, null);
	}

	public static void registerCommands() {
		for (final Map.Entry<String, Command> entry : CloudSpigotConfig.commands.entrySet()) {
			MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), "CloudSpigot",
					entry.getValue());
		}
	}

	static void readConfig(Class<?> clazz, Object instance) {
		for (final Method method : clazz.getDeclaredMethods()) {
			if (Modifier.isPrivate(method.getModifiers())) {
				if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
					try {
						method.setAccessible(true);
						method.invoke(instance);
					} catch (final InvocationTargetException ex) {
						throw Throwables.propagate(ex.getCause());
					} catch (final Exception ex) {
						Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
					}
				}
			}
		}

		try {
			CloudSpigotConfig.config.save(CloudSpigotConfig.CONFIG_FILE);
		} catch (final IOException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CloudSpigotConfig.CONFIG_FILE, ex);
		}
	}

	private static void set(String path, Object val) {
		CloudSpigotConfig.config.set(path, val);
	}

	private static boolean getBoolean(String path, boolean def) {
		CloudSpigotConfig.config.addDefault(path, def);
		return CloudSpigotConfig.config.getBoolean(path, CloudSpigotConfig.config.getBoolean(path));
	}

	private static double getDouble(String path, double def) {
		CloudSpigotConfig.config.addDefault(path, def);
		return CloudSpigotConfig.config.getDouble(path, CloudSpigotConfig.config.getDouble(path));
	}

	private static float getFloat(String path, float def) {
		// TODO: Figure out why getFloat() always returns the default value.
		return (float) CloudSpigotConfig.getDouble(path, def);
	}

	private static int getInt(String path, int def) {
		CloudSpigotConfig.config.addDefault(path, def);
		return CloudSpigotConfig.config.getInt(path, CloudSpigotConfig.config.getInt(path));
	}

	@SuppressWarnings({ "rawtypes" })
	private static <T> List getList(String path, T def) {
		CloudSpigotConfig.config.addDefault(path, def);
		return CloudSpigotConfig.config.getList(path, CloudSpigotConfig.config.getList(path));
	}

	private static String getString(String path, String def) {
		CloudSpigotConfig.config.addDefault(path, def);
		return CloudSpigotConfig.config.getString(path, CloudSpigotConfig.config.getString(path));
	}

	public static double babyZombieMovementSpeed;

	private static void babyZombieMovementSpeed() {
		CloudSpigotConfig.babyZombieMovementSpeed = CloudSpigotConfig.getDouble("settings.baby-zombie-movement-speed",
				0.5D); // Player moves at 0.1F, for
		// reference
	}

	public static boolean interactLimitEnabled;

	private static void interactLimitEnabled() {
		CloudSpigotConfig.interactLimitEnabled = CloudSpigotConfig.getBoolean("settings.limit-player-interactions",
				true);
		if (!CloudSpigotConfig.interactLimitEnabled) {
			Bukkit.getLogger().log(Level.INFO,
					"Disabling player interaction limiter, your server may be more vulnerable to malicious users");
		}
	}

	public static boolean animateExplosions;

	private static void animateExplosions() {
		CloudSpigotConfig.animateExplosions = CloudSpigotConfig.getBoolean("settings.animate-explosions", false);
	}

	public static boolean autoRespawnPlayers;

	private static void autoRespawnPlayers() {
		CloudSpigotConfig.autoRespawnPlayers = CloudSpigotConfig.getBoolean("settings.autorespawn-players", false);
	}

	public static double strengthEffectModifier;
	public static double weaknessEffectModifier;

	private static void effectModifiers() {
		CloudSpigotConfig.strengthEffectModifier = CloudSpigotConfig.getDouble("effect-modifiers.strength", 1.3D);
		CloudSpigotConfig.weaknessEffectModifier = CloudSpigotConfig.getDouble("effect-modifiers.weakness", -0.5D);
	}

	public static Set<Integer> dataValueAllowedItems;

	@SuppressWarnings("unchecked")
	private static void dataValueAllowedItems() {
		CloudSpigotConfig.dataValueAllowedItems = new HashSet<Integer>(
				CloudSpigotConfig.getList("data-value-allowed-items", Collections.emptyList()));
		Bukkit.getLogger()
				.info("Data value allowed items: " + StringUtils.join(CloudSpigotConfig.dataValueAllowedItems, ", "));
	}

	public static boolean stackableLavaBuckets;
	public static boolean stackableWaterBuckets;
	public static boolean stackableMilkBuckets;

	private static void stackableBuckets() {
		CloudSpigotConfig.stackableLavaBuckets = CloudSpigotConfig.getBoolean("stackable-buckets.lava", false);
		CloudSpigotConfig.stackableWaterBuckets = CloudSpigotConfig.getBoolean("stackable-buckets.water", false);
		CloudSpigotConfig.stackableMilkBuckets = CloudSpigotConfig.getBoolean("stackable-buckets.milk", false);

		Field maxStack;

		try {
			maxStack = Material.class.getDeclaredField("maxStack");
			maxStack.setAccessible(true);

			final Field modifiers = Field.class.getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(maxStack, maxStack.getModifiers() & ~Modifier.FINAL);
		} catch (final Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			if (CloudSpigotConfig.stackableLavaBuckets) {
				maxStack.set(Material.LAVA_BUCKET, Material.BUCKET.getMaxStackSize());
				Items.LAVA_BUCKET.c(Material.BUCKET.getMaxStackSize());
			}

			if (CloudSpigotConfig.stackableWaterBuckets) {
				maxStack.set(Material.WATER_BUCKET, Material.BUCKET.getMaxStackSize());
				Items.WATER_BUCKET.c(Material.BUCKET.getMaxStackSize());
			}

			if (CloudSpigotConfig.stackableMilkBuckets) {
				maxStack.set(Material.MILK_BUCKET, Material.BUCKET.getMaxStackSize());
				Items.MILK_BUCKET.c(Material.BUCKET.getMaxStackSize());
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean warnForExcessiveVelocity;

	private static void excessiveVelocityWarning() {
		CloudSpigotConfig.warnForExcessiveVelocity = CloudSpigotConfig.getBoolean("warnWhenSettingExcessiveVelocity",
				true);
	}
}
