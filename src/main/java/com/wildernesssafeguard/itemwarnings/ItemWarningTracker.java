package com.wildernesssafeguard.itemwarnings;

import com.wildernesssafeguard.WildernessSafeguardConfig;
import java.util.EnumMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

/**
 * Tracks which enabled risky items are in the inventory or equipment, and shows
 * an infobox for each one so the player knows what to bank.
 */
@Singleton
public class ItemWarningTracker
{
	private final Client client;
	private final ClientThread clientThread;
	private final ItemManager itemManager;
	private final InfoBoxManager infoBoxManager;
	private final WildernessSafeguardConfig config;

	/** Enabled risky items currently carried, with the item ID used for the infobox icon. */
	private final Map<RiskyItem, Integer> carried = new EnumMap<>(RiskyItem.class);
	private final Map<RiskyItem, RiskyItemInfoBox> infoBoxes = new EnumMap<>(RiskyItem.class);

	private Plugin plugin;

	@Inject
	ItemWarningTracker(Client client, ClientThread clientThread, ItemManager itemManager,
		InfoBoxManager infoBoxManager, WildernessSafeguardConfig config)
	{
		this.client = client;
		this.clientThread = clientThread;
		this.itemManager = itemManager;
		this.infoBoxManager = infoBoxManager;
		this.config = config;
	}

	public void startUp(Plugin plugin)
	{
		this.plugin = plugin;
		clientThread.invokeLater(this::refresh);
	}

	public void shutDown()
	{
		carried.clear();
		updateInfoBoxes();
	}

	public boolean isCarryingRiskyItem()
	{
		return !carried.isEmpty();
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() == InventoryID.INV || event.getContainerId() == InventoryID.WORN)
		{
			refresh();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (WildernessSafeguardConfig.GROUP.equals(event.getGroup()))
		{
			clientThread.invokeLater(this::refresh);
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING)
		{
			carried.clear();
			updateInfoBoxes();
		}
	}

	/** Must run on the client thread. */
	private void refresh()
	{
		carried.clear();
		scan(client.getItemContainer(InventoryID.INV));
		scan(client.getItemContainer(InventoryID.WORN));
		updateInfoBoxes();
	}

	private void scan(ItemContainer container)
	{
		if (container == null)
		{
			return;
		}
		for (Item item : container.getItems())
		{
			if (item.getId() <= 0)
			{
				continue;
			}
			RiskyItem risky = RiskyItem.fromItemName(itemManager.getItemComposition(item.getId()).getName());
			if (risky != null && risky.isEnabled(config))
			{
				carried.putIfAbsent(risky, item.getId());
			}
		}
	}

	private void updateInfoBoxes()
	{
		infoBoxes.entrySet().removeIf(e ->
		{
			if (!carried.containsKey(e.getKey()))
			{
				infoBoxManager.removeInfoBox(e.getValue());
				return true;
			}
			return false;
		});

		if (plugin == null)
		{
			return;
		}
		carried.forEach((item, itemId) -> infoBoxes.computeIfAbsent(item, i ->
		{
			RiskyItemInfoBox box = new RiskyItemInfoBox(itemManager.getImage(itemId), plugin, i);
			infoBoxManager.addInfoBox(box);
			return box;
		}));
	}
}
