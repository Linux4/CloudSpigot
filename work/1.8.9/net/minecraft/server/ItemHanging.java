package net.minecraft.server;

public class ItemHanging extends Item {

    private final Class<? extends EntityHanging> a;

    public ItemHanging(Class<? extends EntityHanging> oclass) {
        this.a = oclass;
        this.a(CreativeModeTab.c);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2) {
        if (enumdirection == EnumDirection.DOWN) {
            return false;
        } else if (enumdirection == EnumDirection.UP) {
            return false;
        } else {
            BlockPosition blockposition1 = blockposition.shift(enumdirection);

            if (!entityhuman.a(blockposition1, enumdirection, itemstack)) {
                return false;
            } else {
                EntityHanging entityhanging = this.a(world, blockposition1, enumdirection);

                if (entityhanging != null && entityhanging.survives()) {
                    if (!world.isClientSide) {
                        world.addEntity(entityhanging);
                    }

                    --itemstack.count;
                }

                return true;
            }
        }
    }

    private EntityHanging a(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        return (EntityHanging) (this.a == EntityPainting.class ? new EntityPainting(world, blockposition, enumdirection) : (this.a == EntityItemFrame.class ? new EntityItemFrame(world, blockposition, enumdirection) : null));
    }
}
