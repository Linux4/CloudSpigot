package net.minecraft.server;

public class ShapedRecipes implements IRecipe {

    private final int width;
    private final int height;
    private final ItemStack[] items;
    private final ItemStack result;
    private boolean e;

    public ShapedRecipes(int i, int j, ItemStack[] aitemstack, ItemStack itemstack) {
        this.width = i;
        this.height = j;
        this.items = aitemstack;
        this.result = itemstack;
    }

    public ItemStack b() {
        return this.result;
    }

    public ItemStack[] b(InventoryCrafting inventorycrafting) {
        ItemStack[] aitemstack = new ItemStack[inventorycrafting.getSize()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = inventorycrafting.getItem(i);

            if (itemstack != null && itemstack.getItem().r()) {
                aitemstack[i] = new ItemStack(itemstack.getItem().q());
            }
        }

        return aitemstack;
    }

    public boolean a(InventoryCrafting inventorycrafting, World world) {
        for (int i = 0; i <= 3 - this.width; ++i) {
            for (int j = 0; j <= 3 - this.height; ++j) {
                if (this.a(inventorycrafting, i, j, true)) {
                    return true;
                }

                if (this.a(inventorycrafting, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean a(InventoryCrafting inventorycrafting, int i, int j, boolean flag) {
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 3; ++l) {
                int i1 = k - i;
                int j1 = l - j;
                ItemStack itemstack = null;

                if (i1 >= 0 && j1 >= 0 && i1 < this.width && j1 < this.height) {
                    if (flag) {
                        itemstack = this.items[this.width - i1 - 1 + j1 * this.width];
                    } else {
                        itemstack = this.items[i1 + j1 * this.width];
                    }
                }

                ItemStack itemstack1 = inventorycrafting.c(k, l);

                if (itemstack1 != null || itemstack != null) {
                    if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null) {
                        return false;
                    }

                    if (itemstack.getItem() != itemstack1.getItem()) {
                        return false;
                    }

                    if (itemstack.getData() != 32767 && itemstack.getData() != itemstack1.getData()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public ItemStack craftItem(InventoryCrafting inventorycrafting) {
        ItemStack itemstack = this.b().cloneItemStack();

        if (this.e) {
            for (int i = 0; i < inventorycrafting.getSize(); ++i) {
                ItemStack itemstack1 = inventorycrafting.getItem(i);

                if (itemstack1 != null && itemstack1.hasTag()) {
                    itemstack.setTag((NBTTagCompound) itemstack1.getTag().clone());
                }
            }
        }

        return itemstack;
    }

    public int a() {
        return this.width * this.height;
    }
}
