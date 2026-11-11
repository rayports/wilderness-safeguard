package com.wildernesssafeguard.highrisk;

import com.wildernesssafeguard.WildernessSafeguardConfig;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Menu;
import net.runelite.api.MenuEntry;
import net.runelite.api.WorldType;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.Text;

/**
 * Removes menu entries that would teleport the player into the Wilderness
 * while logged into a High Risk world.
 */
@Singleton
public class HighRiskTeleportBlocker
{
	/**
	 * Options that never teleport, so items like a Burning amulet can still be
	 * equipped, banked, dropped, etc. Anything starting with one of these is kept.
	 */
	private static final Set<String> SAFE_OPTION_PREFIXES = Set.of(
		"examine", "drop", "use", "wield", "wear", "remove", "cancel", "take",
		"walk here", "destroy", "withdraw", "deposit", "value", "buy", "sell",
		"store", "check", "offer"
	);

	private final Client client;
	private final WildernessSafeguardConfig config;

	@Inject
	HighRiskTeleportBlocker(Client client, WildernessSafeguardConfig config)
	{
		this.client = client;
		this.config = config;
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (!client.getWorldType().contains(WorldType.HIGH_RISK))
		{
			return;
		}

		String option = Text.removeTags(event.getOption()).toLowerCase();
		if (isSafeOption(option))
		{
			return;
		}

		String target = Text.removeTags(event.getTarget()).toLowerCase();
		if (config.blockPohObelisk() && isPohObeliskTeleport(option, target))
		{
			remove(event.getMenuEntry());
			return;
		}

		for (WildernessTeleport teleport : WildernessTeleport.values())
		{
			if (teleport.isEnabled(config) && teleport.matches(option, target))
			{
				remove(event.getMenuEntry());
				return;
			}
		}
	}

	/**
	 * Entries can live in a submenu (e.g. an item's "Teleport" submenu) rather than the
	 * top-level menu, and Menu.removeMenuEntry throws if the entry isn't directly in it.
	 */
	private void remove(MenuEntry entry)
	{
		removeFrom(client.getMenu(), entry);
	}

	private static boolean removeFrom(Menu menu, MenuEntry entry)
	{
		MenuEntry[] entries = menu.getMenuEntries();
		for (MenuEntry e : entries)
		{
			if (e == entry)
			{
				menu.removeMenuEntry(entry);
				return true;
			}
		}
		for (MenuEntry e : entries)
		{
			Menu subMenu = e.getSubMenu();
			if (subMenu != null && removeFrom(subMenu, entry))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * The house obelisk is only reachable inside the POH instance. The real obelisks are
	 * already in the Wilderness and aren't instanced, so they're left alone.
	 */
	private boolean isPohObeliskTeleport(String option, String target)
	{
		return target.contains("obelisk")
			&& (option.startsWith("activate") || option.startsWith("teleport"))
			&& client.getTopLevelWorldView().isInstance();
	}

	private static boolean isSafeOption(String option)
	{
		for (String prefix : SAFE_OPTION_PREFIXES)
		{
			if (option.startsWith(prefix))
			{
				return true;
			}
		}
		return false;
	}
}
