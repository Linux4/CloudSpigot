package net.minecraft.server;

import java.util.Iterator;

public class WorldManager implements IWorldAccess {

	private MinecraftServer a;
	private WorldServer world;

	public WorldManager(MinecraftServer minecraftserver, WorldServer worldserver) {
		this.a = minecraftserver;
		this.world = worldserver;
	}

	@Override
	public void a(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {
	}

	@Override
	public void a(Entity entity) {
		this.world.getTracker().track(entity);
	}

	@Override
	public void b(Entity entity) {
		this.world.getTracker().untrackEntity(entity);
		this.world.getScoreboard().a(entity);
	}

	@Override
	public void a(String s, double d0, double d1, double d2, float f, float f1) {
		// CraftBukkit - this.world.dimension
		this.a.getPlayerList().sendPacketNearby(d0, d1, d2, f > 1.0F ? (double) (16.0F * f) : 16.0D,
				this.world.dimension, new PacketPlayOutNamedSoundEffect(s, d0, d1, d2, f, f1));
	}

	@Override
	public void a(EntityHuman entityhuman, String s, double d0, double d1, double d2, float f, float f1) {
		// CraftBukkit - this.world.dimension
		this.a.getPlayerList().sendPacketNearby(entityhuman, d0, d1, d2, f > 1.0F ? (double) (16.0F * f) : 16.0D,
				this.world.dimension, new PacketPlayOutNamedSoundEffect(s, d0, d1, d2, f, f1));
	}

	@Override
	public void a(int i, int j, int k, int l, int i1, int j1) {
	}

	@Override
	public void a(BlockPosition blockposition) {
		this.world.getPlayerChunkMap().flagDirty(blockposition);
	}

	@Override
	public void b(BlockPosition blockposition) {
	}

	@Override
	public void a(String s, BlockPosition blockposition) {
	}

	@Override
	public void a(EntityHuman entityhuman, int i, BlockPosition blockposition, int j) {
		// CraftBukkit - this.world.dimension
		this.a.getPlayerList().sendPacketNearby(entityhuman, blockposition.getX(), blockposition.getY(),
				blockposition.getZ(), 64.0D, this.world.dimension,
				new PacketPlayOutWorldEvent(i, blockposition, j, false));
	}

	@Override
	public void a(int i, BlockPosition blockposition, int j) {
		this.a.getPlayerList().sendAll(new PacketPlayOutWorldEvent(i, blockposition, j, true));
	}

	@Override
	public void b(int i, BlockPosition blockposition, int j) {
		Iterator<EntityPlayer> iterator = this.a.getPlayerList().v().iterator();

		// CraftBukkit start
		EntityHuman entityhuman = null;
		Entity entity = world.a(i); // PAIL Rename getEntity
		if (entity instanceof EntityHuman)
			entityhuman = (EntityHuman) entity;
		// CraftBukkit end

		while (iterator.hasNext()) {
			EntityPlayer entityplayer = iterator.next();

			if (entityplayer != null && entityplayer.world == this.world && entityplayer.getId() != i) {
				double d0 = blockposition.getX() - entityplayer.locX;
				double d1 = blockposition.getY() - entityplayer.locY;
				double d2 = blockposition.getZ() - entityplayer.locZ;

				// CraftBukkit start
				if (entityhuman != null && entityhuman instanceof EntityPlayer
						&& !entityplayer.getBukkitEntity().canSee(((EntityPlayer) entityhuman).getBukkitEntity())) {
					continue;
				}
				// CraftBukkit end

				if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D) {
					entityplayer.playerConnection.sendPacket(new PacketPlayOutBlockBreakAnimation(i, blockposition, j));
				}
			}
		}

	}
}
