package net.minecraft.server;

public class ItemFlintAndSteel extends Item {

    public ItemFlintAndSteel() {
        this.maxStackSize = 1;
        this.setMaxDurability(64);
        this.a(CreativeModeTab.i);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2) {
        blockposition = blockposition.shift(enumdirection);
        if (!entityhuman.a(blockposition, enumdirection, itemstack)) {
            return false;
        } else {
            if (world.getType(blockposition).getBlock().getMaterial() == Material.AIR) {
                world.makeSound((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, "fire.ignite", 1.0F, ItemFlintAndSteel.g.nextFloat() * 0.4F + 0.8F);
                world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
            }

            itemstack.damage(1, entityhuman);
            return true;
        }
    }
}
