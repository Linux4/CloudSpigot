package net.minecraft.server;

public class ItemBucket extends Item {

    private Block a;

    public ItemBucket(Block block) {
        this.maxStackSize = 1;
        this.a = block;
        this.a(CreativeModeTab.f);
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        boolean flag = this.a == Blocks.AIR;
        MovingObjectPosition movingobjectposition = this.a(world, entityhuman, flag);

        if (movingobjectposition == null) {
            return itemstack;
        } else {
            if (movingobjectposition.type == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
                BlockPosition blockposition = movingobjectposition.a();

                if (!world.a(entityhuman, blockposition)) {
                    return itemstack;
                }

                if (flag) {
                    if (!entityhuman.a(blockposition.shift(movingobjectposition.direction), movingobjectposition.direction, itemstack)) {
                        return itemstack;
                    }

                    IBlockData iblockdata = world.getType(blockposition);
                    Material material = iblockdata.getBlock().getMaterial();

                    if (material == Material.WATER && ((Integer) iblockdata.get(BlockFluids.LEVEL)).intValue() == 0) {
                        world.setAir(blockposition);
                        entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
                        return this.a(itemstack, entityhuman, Items.WATER_BUCKET);
                    }

                    if (material == Material.LAVA && ((Integer) iblockdata.get(BlockFluids.LEVEL)).intValue() == 0) {
                        world.setAir(blockposition);
                        entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
                        return this.a(itemstack, entityhuman, Items.LAVA_BUCKET);
                    }
                } else {
                    if (this.a == Blocks.AIR) {
                        return new ItemStack(Items.BUCKET);
                    }

                    BlockPosition blockposition1 = blockposition.shift(movingobjectposition.direction);

                    if (!entityhuman.a(blockposition1, movingobjectposition.direction, itemstack)) {
                        return itemstack;
                    }

                    if (this.a(world, blockposition1) && !entityhuman.abilities.canInstantlyBuild) {
                        entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
                        return new ItemStack(Items.BUCKET);
                    }
                }
            }

            return itemstack;
        }
    }

    private ItemStack a(ItemStack itemstack, EntityHuman entityhuman, Item item) {
        if (entityhuman.abilities.canInstantlyBuild) {
            return itemstack;
        } else if (--itemstack.count <= 0) {
            return new ItemStack(item);
        } else {
            if (!entityhuman.inventory.pickup(new ItemStack(item))) {
                entityhuman.drop(new ItemStack(item, 1, 0), false);
            }

            return itemstack;
        }
    }

    public boolean a(World world, BlockPosition blockposition) {
        if (this.a == Blocks.AIR) {
            return false;
        } else {
            Material material = world.getType(blockposition).getBlock().getMaterial();
            boolean flag = !material.isBuildable();

            if (!world.isEmpty(blockposition) && !flag) {
                return false;
            } else {
                if (world.worldProvider.n() && this.a == Blocks.FLOWING_WATER) {
                    int i = blockposition.getX();
                    int j = blockposition.getY();
                    int k = blockposition.getZ();

                    world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), "random.fizz", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l) {
                        world.addParticle(EnumParticle.SMOKE_LARGE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                } else {
                    if (!world.isClientSide && flag && !material.isLiquid()) {
                        world.setAir(blockposition, true);
                    }

                    world.setTypeAndData(blockposition, this.a.getBlockData(), 3);
                }

                return true;
            }
        }
    }
}
