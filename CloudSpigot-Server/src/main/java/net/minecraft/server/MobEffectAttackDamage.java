package net.minecraft.server;

public class MobEffectAttackDamage extends MobEffectList {

    protected MobEffectAttackDamage(int i, MinecraftKey minecraftkey, boolean flag, int j) {
        super(i, minecraftkey, flag, j);
    }

    public double a(int i, AttributeModifier attributemodifier) {
        // CloudSpigot - Configurable modifiers for strength and weakness effects
        return this.id == MobEffectList.WEAKNESS.id ? (double) (org.github.paperspigot.CloudSpigotConfig.weaknessEffectModifier * (float) (i + 1)) : org.github.paperspigot.CloudSpigotConfig.strengthEffectModifier * (double) (i + 1);
    }
}
