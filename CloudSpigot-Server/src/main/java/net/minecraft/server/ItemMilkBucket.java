package net.minecraft.server;

import org.github.paperspigot.CloudSpigotConfig; // CloudSpigot

public class ItemMilkBucket extends Item {

    public ItemMilkBucket() {
        this.c(1);
        this.a(CreativeModeTab.f);
    }

    public ItemStack b(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (!entityhuman.abilities.canInstantlyBuild) {
            --itemstack.count;
        }

        if (!world.isClientSide) {
            entityhuman.removeAllEffects();
        }

        entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
        // CloudSpigot start - Stackable Buckets
        if (CloudSpigotConfig.stackableMilkBuckets) {
            if (itemstack.count <= 0) {
                return new ItemStack(Items.BUCKET);
            } else if (!entityhuman.inventory.pickup(new ItemStack(Items.BUCKET))) {
                entityhuman.drop(new ItemStack(Items.BUCKET), false);
            }
        }
        // CloudSpigot end
        return itemstack.count <= 0 ? new ItemStack(Items.BUCKET) : itemstack;
    }

    public int d(ItemStack itemstack) {
        return 32;
    }

    public EnumAnimation e(ItemStack itemstack) {
        return EnumAnimation.DRINK;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        entityhuman.a(itemstack, this.d(itemstack));
        return itemstack;
    }
}
