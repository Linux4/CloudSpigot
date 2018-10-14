package eu.server24_7.cloudspigot;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

public class AnimatedExplosion {

	@SuppressWarnings({ "deprecation" })
	public AnimatedExplosion(List<Block> blockList) {
		for (Block b : blockList) {
			Material material = b.getType();
			if (material != Material.AIR && material != Material.TNT) {
				FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
				b.setType(Material.AIR);
				fb.setDropItem(true);
				fb.setCustomName("AnimatedExplosion");
				fb.setCustomNameVisible(false);
				float x = -0.25F + (float) (Math.random() * ((0.25 - -0.25) + 0.25));
				float y = -0.25F + (float) (Math.random() * ((0.25 - -0.25) + 0.25));
				float z = -0.25F + (float) (Math.random() * ((0.25 - -0.25) + 0.25));
				fb.setVelocity(new Vector(x, y, z));
			}
		}

	}
}
