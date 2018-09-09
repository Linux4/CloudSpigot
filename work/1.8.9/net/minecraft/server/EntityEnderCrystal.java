package net.minecraft.server;

public class EntityEnderCrystal extends Entity {

    public int a;
    public int b;

    public EntityEnderCrystal(World world) {
        super(world);
        this.k = true;
        this.setSize(2.0F, 2.0F);
        this.b = 5;
        this.a = this.random.nextInt(100000);
    }

    protected boolean s_() {
        return false;
    }

    protected void h() {
        this.datawatcher.a(8, Integer.valueOf(this.b));
    }

    public void t_() {
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        ++this.a;
        this.datawatcher.watch(8, Integer.valueOf(this.b));
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locY);
        int k = MathHelper.floor(this.locZ);

        if (this.world.worldProvider instanceof WorldProviderTheEnd && this.world.getType(new BlockPosition(i, j, k)).getBlock() != Blocks.FIRE) {
            this.world.setTypeUpdate(new BlockPosition(i, j, k), Blocks.FIRE.getBlockData());
        }

    }

    protected void b(NBTTagCompound nbttagcompound) {}

    protected void a(NBTTagCompound nbttagcompound) {}

    public boolean ad() {
        return true;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            if (!this.dead && !this.world.isClientSide) {
                this.b = 0;
                if (this.b <= 0) {
                    this.die();
                    if (!this.world.isClientSide) {
                        this.world.explode((Entity) null, this.locX, this.locY, this.locZ, 6.0F, true);
                    }
                }
            }

            return true;
        }
    }
}
