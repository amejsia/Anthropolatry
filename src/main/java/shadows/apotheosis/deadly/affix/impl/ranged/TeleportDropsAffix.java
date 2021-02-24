package shadows.apotheosis.deadly.affix.impl.ranged;

import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import shadows.apotheosis.deadly.affix.Affix;
import shadows.apotheosis.deadly.affix.EquipmentType;
import shadows.apotheosis.deadly.affix.modifiers.AffixModifier;

/**
 * Drops from killed enemies are teleported to the shooter.
 */
public class TeleportDropsAffix extends Affix {

	public TeleportDropsAffix(int weight) {
		super(weight);
	}

	@Override
	public float generateLevel(ItemStack stack, Random rand, AffixModifier modifier) {
		int lvl = 2 + rand.nextInt(5);
		if (modifier != null) lvl = (int) modifier.editLevel(this, lvl);
		return lvl;
	}

	@Override
	public void addInformation(ItemStack stack, float level, Consumer<ITextComponent> list) {
		list.accept(new TranslationTextComponent("affix." + this.getRegistryName() + ".desc", level));
	}

	@Override
	public boolean canApply(EquipmentType type) {
		return type == EquipmentType.RANGED;
	}

	@Override
	public float getMin() {
		return 1;
	}

	@Override
	public float getMax() {
		return 64;
	}

}