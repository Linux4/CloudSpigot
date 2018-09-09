package net.minecraft.server;

public class BlockDropper extends BlockDispenser {

    private final IDispenseBehavior P = new DispenseBehaviorItem();

    public BlockDropper() {}

    protected IDispenseBehavior a(ItemStack itemstack) {
        return this.P;
    }

    public TileEntity a(World world, int i) {
        return new TileEntityDropper();
    }

    public void dispense(World world, BlockPosition blockposition) {
        SourceBlock sourceblock = new SourceBlock(world, blockposition);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) sourceblock.getTileEntity();

        if (tileentitydispenser != null) {
            int i = tileentitydispenser.m();

            if (i < 0) {
                world.triggerEffect(1001, blockposition, 0);
            } else {
                ItemStack itemstack = tileentitydispenser.getItem(i);

                if (itemstack != null) {
                    EnumDirection enumdirection = (EnumDirection) world.getType(blockposition).get(BlockDropper.FACING);
                    BlockPosition blockposition1 = blockposition.shift(enumdirection);
                    IInventory iinventory = TileEntityHopper.b(world, (double) blockposition1.getX(), (double) blockposition1.getY(), (double) blockposition1.getZ());
                    ItemStack itemstack1;

                    if (iinventory == null) {
                        itemstack1 = this.P.a(sourceblock, itemstack);
                        if (itemstack1 != null && itemstack1.count <= 0) {
                            itemstack1 = null;
                        }
                    } else {
                        itemstack1 = TileEntityHopper.addItem(iinventory, itemstack.cloneItemStack().cloneAndSubtract(1), enumdirection.opposite());
                        if (itemstack1 == null) {
                            itemstack1 = itemstack.cloneItemStack();
                            if (--itemstack1.count <= 0) {
                                itemstack1 = null;
                            }
                        } else {
                            itemstack1 = itemstack.cloneItemStack();
                        }
                    }

                    tileentitydispenser.setItem(i, itemstack1);
                }
            }
        }
    }
}
