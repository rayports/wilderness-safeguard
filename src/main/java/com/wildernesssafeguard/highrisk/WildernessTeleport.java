package com.wildernesssafeguard.highrisk;

import com.wildernesssafeguard.WildernessSafeguardConfig;
import java.util.List;
import java.util.function.Predicate;

/**
 * Teleports that land the player in the Wilderness. Each one is matched by
 * keywords that appear in a menu entry's option or target (lowercase, tags stripped),
 * which covers spells ("Cast"), tablets ("Break"), items, POH portals and the portal nexus.
 */
enum WildernessTeleport
{
	ANCIENT(WildernessSafeguardConfig::blockAncientTeleports,
		List.of("carrallanger", "dareeyak", "annakarl", "ghorrock")),
	ICE_PLATEAU(WildernessSafeguardConfig::blockIcePlateau,
		List.of("ice plateau")),
	CEMETERY(WildernessSafeguardConfig::blockCemetery,
		List.of("cemetery teleport")),
	BURNING_AMULET(WildernessSafeguardConfig::blockBurningAmulet,
		List.of("burning amulet", "chaos temple", "bandit camp", "lava maze")),
	WILDERNESS_SWORD(WildernessSafeguardConfig::blockWildernessSword,
		List.of("wilderness sword", "fountain of rune")),
	REVENANT_CAVE(WildernessSafeguardConfig::blockRevenantCaveScroll,
		List.of("revenant cave teleport")),
	WILDERNESS_CRABS(WildernessSafeguardConfig::blockWildernessCrabs,
		List.of("wilderness crabs teleport"));

	private final Predicate<WildernessSafeguardConfig> enabled;
	private final List<String> keywords;

	WildernessTeleport(Predicate<WildernessSafeguardConfig> enabled, List<String> keywords)
	{
		this.enabled = enabled;
		this.keywords = keywords;
	}

	boolean isEnabled(WildernessSafeguardConfig config)
	{
		return enabled.test(config);
	}

	boolean matches(String option, String target)
	{
		for (String keyword : keywords)
		{
			if (option.contains(keyword) || target.contains(keyword))
			{
				return true;
			}
		}
		return false;
	}
}
