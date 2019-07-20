package org.bukkit.command.defaults;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import eu.minewars.cloudspigot.CloudSpigot;
import eu.minewars.cloudspigot.updater.Updater;

public class UpdateCommand extends BukkitCommand {

	public UpdateCommand(String name) {
		super(name);

		this.description = "Update CloudSpigot";
		this.usageMessage = "/update";
		this.setPermission("cloudspigot.command.update");
		this.setAliases(Arrays.asList("updater"));
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender)) {
			return true;
		}

		switch (Updater.checkUpdate()) {
		case ERROR:
			sender.sendMessage(CloudSpigot.serverPrefix
					+ "§cError while trying to check for updates, see console log for more info");
			break;
		case LATEST_VERSION:
			sender.sendMessage(CloudSpigot.serverPrefix + "§9No updates available");
			break;
		case UNKNOWN_VERSION:
			sender.sendMessage(CloudSpigot.serverPrefix + "§cYou are running an unsupported version!");
			break;
		case UPDATE_AVAILABLE:
			sender.sendMessage(CloudSpigot.serverPrefix + "§9Update found. Downloading..");
			if (Updater.update()) {
				sender.sendMessage(
						CloudSpigot.serverPrefix + "§9Update successfully downloaded, please restart the server");
			} else {
				sender.sendMessage(CloudSpigot.serverPrefix
						+ "§cError while trying to download update, see console log for more info");
			}
			break;
		default:
			break;
		}

		return true;
	}

}
