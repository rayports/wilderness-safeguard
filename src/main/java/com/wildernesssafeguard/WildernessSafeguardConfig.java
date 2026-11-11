package com.wildernesssafeguard;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(WildernessSafeguardConfig.GROUP)
public interface WildernessSafeguardConfig extends Config
{
	String GROUP = "wildernesssafeguard";

	@ConfigSection(
		name = "High Risk Worlds",
		description = "Prevent teleporting into the Wilderness while on a High Risk world",
		position = 0
	)
	String highRiskWorlds = "highRiskWorlds";

	@ConfigItem(
		keyName = "highRiskWorldsInfo",
		name = "<html><div style='width:170px'>On High Risk worlds, removes the menu options for the teleports checked below so you can't accidentally teleport into the Wilderness.</div></html>",
		description = "",
		section = highRiskWorlds,
		position = 0
	)
	default void highRiskWorldsInfo()
	{
	}

	@ConfigItem(
		keyName = "blockAncientTeleports",
		name = "Ancient wildy teleports",
		description = "Remove Carrallanger, Dareeyak, Annakarl and Ghorrock teleports (spells, tablets, POH portals)",
		section = highRiskWorlds,
		position = 1
	)
	default boolean blockAncientTeleports()
	{
		return false;
	}

	@ConfigItem(
		keyName = "blockIcePlateau",
		name = "Ice Plateau teleport",
		description = "Remove the Lunar Ice Plateau teleport (spell and tablet)",
		section = highRiskWorlds,
		position = 2
	)
	default boolean blockIcePlateau()
	{
		return false;
	}

	@ConfigItem(
		keyName = "blockCemetery",
		name = "Cemetery teleport",
		description = "Remove the Arceuus Cemetery teleport (spell and tablet)",
		section = highRiskWorlds,
		position = 3
	)
	default boolean blockCemetery()
	{
		return false;
	}

	@ConfigItem(
		keyName = "blockBurningAmulet",
		name = "Burning amulet",
		description = "Remove Burning amulet teleports (Chaos Temple, Bandit Camp, Lava Maze)",
		section = highRiskWorlds,
		position = 4
	)
	default boolean blockBurningAmulet()
	{
		return false;
	}

	@ConfigItem(
		keyName = "blockWildernessSword",
		name = "Wilderness sword",
		description = "Remove the Wilderness sword teleport to the Fountain of Rune",
		section = highRiskWorlds,
		position = 5
	)
	default boolean blockWildernessSword()
	{
		return false;
	}

	@ConfigItem(
		keyName = "blockRevenantCaveScroll",
		name = "Revenant cave teleport",
		description = "Remove the Revenant cave teleport scroll",
		section = highRiskWorlds,
		position = 6
	)
	default boolean blockRevenantCaveScroll()
	{
		return false;
	}

	@ConfigItem(
		keyName = "blockWildernessCrabs",
		name = "Wilderness crabs teleport",
		description = "Remove the Wilderness crabs teleport",
		section = highRiskWorlds,
		position = 7
	)
	default boolean blockWildernessCrabs()
	{
		return false;
	}

	@ConfigItem(
		keyName = "blockPohObelisk",
		name = "POH wilderness obelisk",
		description = "Remove the Activate/Teleport options on the wilderness obelisk in your house",
		section = highRiskWorlds,
		position = 8
	)
	default boolean blockPohObelisk()
	{
		return false;
	}

	@ConfigSection(
		name = "Item Warnings",
		description = "Prevent teleporting into the Wilderness while carrying items you'd lose",
		position = 1
	)
	String itemWarnings = "itemWarnings";

	@ConfigItem(
		keyName = "itemWarningsInfo",
		name = "<html><div style='width:170px'>While you carry a checked item, all Wilderness teleports are removed on every world and an infobox shows the item you need to bank.</div></html>",
		description = "",
		section = itemWarnings,
		position = 0
	)
	default void itemWarningsInfo()
	{
	}

	@ConfigItem(
		keyName = "warnImbuedHeart",
		name = "Imbued heart",
		description = "Block Wilderness teleports while carrying an Imbued heart",
		section = itemWarnings,
		position = 1
	)
	default boolean warnImbuedHeart()
	{
		return false;
	}

	@ConfigItem(
		keyName = "warnSaturatedHeart",
		name = "Saturated heart",
		description = "Block Wilderness teleports while carrying a Saturated heart",
		section = itemWarnings,
		position = 2
	)
	default boolean warnSaturatedHeart()
	{
		return false;
	}

	@ConfigItem(
		keyName = "warnChuggingBarrel",
		name = "Chugging barrel",
		description = "Block Wilderness teleports while carrying a Chugging barrel",
		section = itemWarnings,
		position = 3
	)
	default boolean warnChuggingBarrel()
	{
		return false;
	}

	@ConfigSection(
		name = "Escape Teleport",
		description = "Warn when you're not carrying a teleport that works up to Wilderness level 30",
		position = 2
	)
	String escapeTeleport = "escapeTeleport";

	@ConfigItem(
		keyName = "escapeTeleportInfo",
		name = "<html><div style='width:170px'>Whenever you don't have one of the checked items (charged, in inventory or equipped), an infobox and chat message warn you, so you notice before entering the Wilderness.</div></html>",
		description = "",
		section = escapeTeleport,
		position = 0
	)
	default void escapeTeleportInfo()
	{
	}

	@ConfigItem(
		keyName = "warnNoEscapeTeleport",
		name = "Warn without escape teleport",
		description = "Show a warning when you have none of the checked items below",
		section = escapeTeleport,
		position = 1
	)
	default boolean warnNoEscapeTeleport()
	{
		return false;
	}

	@ConfigItem(
		keyName = "escapeRingOfWealth",
		name = "Ring of wealth",
		description = "A charged Ring of wealth counts as an escape teleport",
		section = escapeTeleport,
		position = 2
	)
	default boolean escapeRingOfWealth()
	{
		return true;
	}

	@ConfigItem(
		keyName = "escapeAmuletOfGlory",
		name = "Amulet of glory",
		description = "A charged or eternal Amulet of glory counts as an escape teleport",
		section = escapeTeleport,
		position = 3
	)
	default boolean escapeAmuletOfGlory()
	{
		return true;
	}

	@ConfigItem(
		keyName = "escapeRoyalSeedPod",
		name = "Royal seed pod",
		description = "A Royal seed pod counts as an escape teleport",
		section = escapeTeleport,
		position = 4
	)
	default boolean escapeRoyalSeedPod()
	{
		return true;
	}
}
