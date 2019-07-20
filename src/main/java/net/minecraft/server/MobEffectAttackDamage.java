package net.minecraft.server;

public class MobEffectAttackDamage extends MobEffectList {

	protected MobEffectAttackDamage(int i, MinecraftKey minecraftkey, boolean flag, int j) {
		super(i, minecraftkey, flag, j);
	}

	@Override
	public double a(int i, AttributeModifier attributemodifier) {
		// CloudSpigot - Configurable modifiers for strength and weakness effects
		return this.id == MobEffectList.WEAKNESS.id
				? (double) (eu.minewars.cloudspigot.CloudSpigotConfig.weaknessEffectModifier * (i + 1))
				: eu.minewars.cloudspigot.CloudSpigotConfig.strengthEffectModifier * (i + 1);
	}
}
