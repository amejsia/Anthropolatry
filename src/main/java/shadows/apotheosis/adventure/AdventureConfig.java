package shadows.apotheosis.adventure;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.apotheosis.adventure.loot.LootCategory;
import shadows.apotheosis.adventure.loot.LootRarity;
import shadows.placebo.config.Configuration;

public class AdventureConfig {

	//public static final List<ResourceLocation> DIM_WHITELIST = new ArrayList<>();
	//public static final List<ResourceLocation> BIOME_BLACKLIST = new ArrayList<>();
	public static final Map<ResourceLocation, LootCategory> TYPE_OVERRIDES = new HashMap<>();

	//Boss Stats
	//public static int surfaceBossChance = 85;
	//public static boolean surfaceBossLightning = true;
	//public static boolean curseBossItems = false;

	//Generation Chances
	//public static int bossDungeonAttempts = 8;
	//public static int bossDungeon2Attempts = 8;
	//public static int rogueSpawnerAttempts = 4;
	//public static int troveAttempts = 8;
	//public static int tomeTowerChance = 125;

	//public static boolean mythicUnbreakable = true;

	//public static int spawnerValueChance = 9;

	// Affix
	public static float randomAffixItem = 0.07F;
	public static float gemDropChance = 0.04F;
	public static float affixChestChance = 0.75F;
	public static float gemChestChance = 0.55F;
	public static int[] rarityThresholds = new int[] { 400, 700, 850, 940, 995 };
	public static boolean disableQuarkOnAffixItems = true;
	public static Supplier<Item> torchItem = () -> Items.TORCH;

	public static void load(Configuration c) {
		for (LootRarity r : LootRarity.values()) {
			if (r != LootRarity.ANCIENT) {
				int threshold = c.getInt(r.id(), "rarity", rarityThresholds[r.ordinal()], 0, 1000, "The threshold for this rarity.  The percentage chance of this rarity appearing is equal to (previous threshold - this threshold) / 10.");
				rarityThresholds[r.ordinal()] = threshold;
			}
		}

		TYPE_OVERRIDES.clear();
		String[] overrides = c.getStringList("Equipment Type Overrides", "affixes", new String[] { "minecraft:stick|SWORD" }, "A list of type overrides for the affix loot system.  Format is <itemname>|<type>.  Types are SWORD, TRIDENT, SHIELD, HEAVY_WEAPON, BREAKER, CROSSBOW, BOW");
		for (String s : overrides) {
			String[] split = s.split("\\|");
			try {
				LootCategory type = LootCategory.valueOf(split[1].toUpperCase(Locale.ROOT));
				if (type == LootCategory.ARMOR) throw new UnsupportedOperationException("Cannot override an item to type ARMOR!");
				TYPE_OVERRIDES.put(new ResourceLocation(split[0]), type);
			} catch (Exception e) {
				AdventureModule.LOGGER.error("Invalid type override entry: " + s + " will be ignored!");
				e.printStackTrace();
			}
		}

		randomAffixItem = c.getFloat("Random Affix Chance", "affixes", randomAffixItem, 0, 1, "The chance that a naturally spawned mob will be granted an affix item. 0 = 0%, 1 = 100%");
		gemDropChance = c.getFloat("Gem Drop Chance", "affixes", gemDropChance, 0, 1, "The chance that a mob will drop a gem. 0 = 0%, 1 = 100%");

		affixChestChance = c.getFloat("Affix Chest Chance", "affixes", gemChestChance, 0, 1, "The chance that an affix item will be added to a loot chest. 0 = 0%, 1 = 100%");
		gemChestChance = c.getFloat("Gem Chest Chance", "affixes", gemChestChance, 0, 1, "The chance that a gem will be added to a loot chest. 0 = 0%, 1 = 100%");

		disableQuarkOnAffixItems = c.getBoolean("Disable Quark Tooltips for Affix Items", "affixes", true, "If Quark's Attribute Tooltip handling is disabled for affix items");

		String torch = c.getString("Torch Placement Item", "affixes", "minecraft:torch", "The item that will be used when attempting to place torches with the torch placer affix.  Must be a valid item that places a block on right click.");
		torchItem = () -> {
			try {
				Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(torch));
				return i == Items.AIR ? Items.TORCH : i;
			} catch (Exception ex) {
				AdventureModule.LOGGER.error("Invalid torch item {}", torch);
				return Items.TORCH;
			}
		};
	}

}