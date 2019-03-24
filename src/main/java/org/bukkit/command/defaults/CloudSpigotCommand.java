package org.bukkit.command.defaults;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import eu.server24_7.cloudspigot.CloudSpigot;
import eu.server24_7.cloudspigot.updater.Updater;

public class CloudSpigotCommand extends BukkitCommand {

	public CloudSpigotCommand(String name) {
		super(name);

		this.description = "Display CloudSpigot Information";
		this.usageMessage = "/cloudspigot";
		this.setPermission("cloudspigot.command.cloudspigot");
		this.setAliases(Arrays.asList("cs"));
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender)) {
			return true;
		}

		sender.sendMessage(CloudSpigot.serverPrefix + "§9Server Information: ");
		sender.sendMessage(
				CloudSpigot.serverPrefix + "§9Version: §a" + CloudSpigot.class.getPackage().getImplementationVersion());
		sender.sendMessage(CloudSpigot.serverPrefix + "§9API-Version: §a"
				+ CloudSpigot.class.getPackage().getSpecificationVersion());
		sender.sendMessage(
				CloudSpigot.serverPrefix + "§9Author: §a" + CloudSpigot.class.getPackage().getImplementationVendor());
		sender.sendMessage(CloudSpigot.serverPrefix + "§9Java Information: ");
		sender.sendMessage(CloudSpigot.serverPrefix + "§9Java-Version: §a" + Updater.getJavaVersion());
		sender.sendMessage(CloudSpigot.serverPrefix + "§9Threads: §a" + Thread.activeCount());
		sender.sendMessage(CloudSpigot.serverPrefix + "§9Memory (max/used/free): §a"
				+ Runtime.getRuntime().maxMemory() / 1024 / 1024 + "§9/§a"
				+ (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "§9/§a"
				+ Runtime.getRuntime().freeMemory() / 1024 / 1024 + " MB");
		sender.sendMessage(CloudSpigot.serverPrefix + "§9CPU Count: §a" + Runtime.getRuntime().availableProcessors());

		return true;
	}

}
