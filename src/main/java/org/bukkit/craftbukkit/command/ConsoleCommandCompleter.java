package org.bukkit.craftbukkit.command;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Waitable;

// CloudSpigot start - JLine update
import net.minecraft.server.DedicatedServer; // CloudSpigot
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
// CloudSpigot end

public class ConsoleCommandCompleter implements Completer {
	private final DedicatedServer server; // CloudSpigot - CraftServer -> DedicatedServer

	public ConsoleCommandCompleter(DedicatedServer server) { // CloudSpigot - CraftServer -> DedicatedServer
		this.server = server;
	}

	// CloudSpigot start - Change method signature for JLine update
	public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
		final CraftServer server = this.server.server;
		final String buffer = line.line();
		// CloudSpigot end
		Waitable<List<String>> waitable = new Waitable<List<String>>() {
			@Override
			protected List<String> evaluate() {
				return server.getCommandMap().tabComplete(server.getConsoleSender(), buffer);
			}
		};
		server.getServer().processQueue.add(waitable); // CloudSpigot - Remove "this."
		try {
			List<String> offers = waitable.get();
			if (offers == null) {
				return; // CloudSpigot - Method returns void
			}

			// CloudSpigot start - JLine update
			for (String completion : offers) {
				if (completion.isEmpty()) {
					continue;
				}

				candidates.add(new Candidate(completion));
			}
			// CloudSpigot end

			// CloudSpigot start - JLine handles cursor now
			/*
			 * final int lastSpace = buffer.lastIndexOf(' '); if (lastSpace == -1) { return
			 * cursor - buffer.length(); } else { return cursor - (buffer.length() -
			 * lastSpace - 1); }
			 */
			// CloudSpigot end
		} catch (ExecutionException e) {
			server.getLogger().log(Level.WARNING, "Unhandled exception when tab completing", e); // CloudSpigot - remove
																									// "this."
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
