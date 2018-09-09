package net.minecraft.server;

public class ItemMapEmpty extends ItemWorldMapBase {

    protected ItemMapEmpty() {
        this.a(CreativeModeTab.f);
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        ItemStack itemstack1 = new ItemStack(Items.FILLED_MAP, 1, world.b("map"));
        String s = "map_" + itemstack1.getData();
        WorldMap worldmap = new WorldMap(s);

        world.a(s, (PersistentBase) worldmap);
        worldmap.scale = 0;
        worldmap.a(entityhuman.locX, entityhuman.locZ, worldmap.scale);
        worldmap.map = (byte) world.worldProvider.getDimension();
        worldmap.c();
        --itemstack.count;
        if (itemstack.count <= 0) {
            return itemstack1;
        } else {
            if (!entityhuman.inventory.pickup(itemstack1.cloneItemStack())) {
                entityhuman.drop(itemstack1, false);
            }

            entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
            return itemstack;
        }
    }
}
