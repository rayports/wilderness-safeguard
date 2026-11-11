package com.wildernesssafeguard.escape;

import com.wildernesssafeguard.WildernessSafeguardConfig;
import java.util.List;
import java.util.function.Predicate;

/**
 * Teleports that work up to Wilderness level 30. Matched by item name prefix (lowercase);
 * the prefixes only match charged items, e.g. "Ring of wealth (5)" but not an empty "Ring of wealth".
 */
enum EscapeTeleport
{
	RING_OF_WEALTH(WildernessSafeguardConfig::escapeRingOfWealth,
		List.of("ring of wealth (")),
	AMULET_OF_GLORY(WildernessSafeguardConfig::escapeAmuletOfGlory,
		List.of("amulet of glory(", "amulet of eternal glory")),
	ROYAL_SEED_POD(WildernessSafeguardConfig::escapeRoyalSeedPod,
		List.of("royal seed pod"));

	private final Predicate<WildernessSafeguardConfig> enabled;
	private final List<String> namePrefixes;

	EscapeTeleport(Predicate<WildernessSafeguardConfig> enabled, List<String> namePrefixes)
	{
		this.enabled = enabled;
		this.namePrefixes = namePrefixes;
	}

	static boolean isEnabledEscapeItem(String itemName, WildernessSafeguardConfig config)
	{
		String lower = itemName.toLowerCase();
		for (EscapeTeleport teleport : values())
		{
			if (!teleport.enabled.test(config))
			{
				continue;
			}
			for (String prefix : teleport.namePrefixes)
			{
				if (lower.startsWith(prefix))
				{
					return true;
				}
			}
		}
		return false;
	}
}
