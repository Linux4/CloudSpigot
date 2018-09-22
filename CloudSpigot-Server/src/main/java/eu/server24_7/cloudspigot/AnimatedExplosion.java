package eu.server24_7.cloudspigot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

public class AnimatedExplosion {

	@SuppressWarnings({ "deprecation" })
	public AnimatedExplosion(List<Block> blockList) {
		for (Block b : blockList) {
			Material material = b.getType();
			if (material != Material.AIR) {
				byte data = b.getData();
				FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
				b.setType(Material.AIR);
				fb.setDropItem(false);
				fb.setCustomName("AnimatedExplosion");
				fb.setCustomNameVisible(false);
				float x = -1F + (float) (Math.random() * ((1 - -1) + 1));
				float y = -1F + (float) (Math.random() * ((1 - -1) + 1));
				float z = -1F + (float) (Math.random() * ((1 - -1) + 1));
				fb.setVelocity(new Vector(x, y, z));
			}
		}

	}
}
