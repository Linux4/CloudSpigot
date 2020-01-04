package net.minecraft.server;

import eu.server24_7.cloudspigot.CloudSpigotConfig; // CloudSpigot

public class ItemMilkBucket extends Item {

	public ItemMilkBucket() {
		this.c(1);
		this.a(CreativeModeTab.f);
	}

	@Override
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

	@Override
	public int d(ItemStack itemstack) {
		return 32;
	}

	@Override
	public EnumAnimation e(ItemStack itemstack) {
		return EnumAnimation.DRINK;
	}

	@Override
	public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
		entityhuman.a(itemstack, this.d(itemstack));
		return itemstack;
	}
}
