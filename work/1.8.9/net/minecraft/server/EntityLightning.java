package net.minecraft.server;

import java.util.List;

public class EntityLightning extends EntityWeather {

    private int lifeTicks;
    public long a;
    private int c;

    public EntityLightning(World world, double d0, double d1, double d2) {
        super(world);
        this.setPositionRotation(d0, d1, d2, 0.0F, 0.0F);
        this.lifeTicks = 2;
        this.a = this.random.nextLong();
        this.c = this.random.nextInt(3) + 1;
        BlockPosition blockposition = new BlockPosition(this);

        if (!world.isClientSide && world.getGameRules().getBoolean("doFireTick") && (world.getDifficulty() == EnumDifficulty.NORMAL || world.getDifficulty() == EnumDifficulty.HARD) && world.areChunksLoaded(blockposition, 10)) {
            if (world.getType(blockposition).getBlock().getMaterial() == Material.AIR && Blocks.FIRE.canPlace(world, blockposition)) {
                world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
            }

            for (int i = 0; i < 4; ++i) {
                BlockPosition blockposition1 = blockposition.a(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);

                if (world.getType(blockposition1).getBlock().getMaterial() == Material.AIR && Blocks.FIRE.canPlace(world, blockposition1)) {
                    world.setTypeUpdate(blockposition1, Blocks.FIRE.getBlockData());
                }
            }
        }

    }

    public void t_() {
        super.t_();
        if (this.lifeTicks == 2) {
            this.world.makeSound(this.locX, this.locY, this.locZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            this.world.makeSound(this.locX, this.locY, this.locZ, "random.explode", 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.lifeTicks;
        if (this.lifeTicks < 0) {
            if (this.c == 0) {
                this.die();
            } else if (this.lifeTicks < -this.random.nextInt(10)) {
                --this.c;
                this.lifeTicks = 1;
                this.a = this.random.nextLong();
                BlockPosition blockposition = new BlockPosition(this);

                if (!this.world.isClientSide && this.world.getGameRules().getBoolean("doFireTick") && this.world.areChunksLoaded(blockposition, 10) && this.world.getType(blockposition).getBlock().getMaterial() == Material.AIR && Blocks.FIRE.canPlace(this.world, blockposition)) {
                    this.world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
                }
            }
        }

        if (this.lifeTicks >= 0) {
            if (this.world.isClientSide) {
                this.world.d(2);
            } else {
                double d0 = 3.0D;
                List list = this.world.getEntities(this, new AxisAlignedBB(this.locX - d0, this.locY - d0, this.locZ - d0, this.locX + d0, this.locY + 6.0D + d0, this.locZ + d0));

                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    entity.onLightningStrike(this);
                }
            }
        }

    }

    protected void h() {}

    protected void a(NBTTagCompound nbttagcompound) {}

    protected void b(NBTTagCompound nbttagcompound) {}
}
