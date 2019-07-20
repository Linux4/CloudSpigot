package eu.minewars.cloudspigot.console;

import org.bukkit.craftbukkit.command.ConsoleCommandCompleter;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import net.minecraft.server.DedicatedServer;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

public final class CloudSpigotConsole extends SimpleTerminalConsole {

	private final DedicatedServer server;

	public CloudSpigotConsole(DedicatedServer server) {
		this.server = server;
	}

	@Override
	protected LineReader buildReader(LineReaderBuilder builder) {
		return super.buildReader(builder.appName("CloudSpigot").completer(new ConsoleCommandCompleter(this.server)));
	}

	@Override
	protected boolean isRunning() {
		return !this.server.isStopped() && this.server.isRunning();
	}

	@Override
	protected void runCommand(String command) {
		this.server.issueCommand(command, this.server);
	}

	@Override
	protected void shutdown() {
		this.server.safeShutdown();
	}

}
