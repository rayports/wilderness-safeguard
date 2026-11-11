package com.wildernesssafeguard.itemwarnings;

import com.wildernesssafeguard.WildernessSafeguardConfig;
import java.util.function.Predicate;
import lombok.Getter;

/**
 * Items that shouldn't be taken into the Wilderness. Matched by item name prefix
 * (lowercase) so charged/disassembled variants are caught too.
 */
enum RiskyItem
{
	IMBUED_HEART("Imbued heart", WildernessSafeguardConfig::warnImbuedHeart),
	SATURATED_HEART("Saturated heart", WildernessSafeguardConfig::warnSaturatedHeart),
	CHUGGING_BARREL("Chugging barrel", WildernessSafeguardConfig::warnChuggingBarrel);

	@Getter
	private final String displayName;
	private final String namePrefix;
	private final Predicate<WildernessSafeguardConfig> enabled;

	RiskyItem(String displayName, Predicate<WildernessSafeguardConfig> enabled)
	{
		this.displayName = displayName;
		this.namePrefix = displayName.toLowerCase();
		this.enabled = enabled;
	}

	boolean isEnabled(WildernessSafeguardConfig config)
	{
		return enabled.test(config);
	}

	static RiskyItem fromItemName(String name)
	{
		String lower = name.toLowerCase();
		for (RiskyItem item : values())
		{
			if (lower.startsWith(item.namePrefix))
			{
				return item;
			}
		}
		return null;
	}
}
