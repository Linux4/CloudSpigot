package eu.server24_7.cloudspigot;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

/**
 *
 * Class for generating an {@link AnimatedExplosion}
 *
 */
public class AnimatedExplosion {

	/**
	 * Create a new {@link AnimatedExplosion}
	 *
	 * @param blockList The {@link Block}s which will be used in the
	 *                  {@link AnimatedExplosion}
	 */
	@SuppressWarnings({ "deprecation" })
	public AnimatedExplosion(List<Block> blockList) {
		for (final Block b : blockList) {
			final Material material = b.getType();
			if (material != Material.AIR && material != Material.TNT) {
				final FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), material, b.getData());
				b.setType(Material.AIR);
				fb.setDropItem(true);
				fb.setCustomName("AnimatedExplosion");
				fb.setCustomNameVisible(false);
				final float x = -0.25F + (float) (Math.random() * ((0.25 - -0.25) + 0.25));
				final float y = -0.25F + (float) (Math.random() * ((0.25 - -0.25) + 0.25));
				final float z = -0.25F + (float) (Math.random() * ((0.25 - -0.25) + 0.25));
				fb.setVelocity(new Vector(x, y, z));
			}
		}

	}
}
