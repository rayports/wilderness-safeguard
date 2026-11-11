package com.wildernesssafeguard.escape;

import com.wildernesssafeguard.WildernessSafeguardConfig;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GameTick;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;

/**
 * Warns whenever you're not carrying a teleport that works up to Wilderness level 30,
 * so you notice before entering the Wilderness rather than once you're already in it.
 */
@Singleton
public class EscapeTeleportWarning
{
	private final Client client;
	private final ItemManager itemManager;
	private final InfoBoxManager infoBoxManager;
	private final ChatMessageManager chatMessageManager;
	private final WildernessSafeguardConfig config;

	private Plugin plugin;
	private InfoBox infoBox;

	@Inject
	EscapeTeleportWarning(Client client, ItemManager itemManager, InfoBoxManager infoBoxManager,
		ChatMessageManager chatMessageManager, WildernessSafeguardConfig config)
	{
		this.client = client;
		this.itemManager = itemManager;
		this.infoBoxManager = infoBoxManager;
		this.chatMessageManager = chatMessageManager;
		this.config = config;
	}

	public void startUp(Plugin plugin)
	{
		this.plugin = plugin;
	}

	public void shutDown()
	{
		hide();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		boolean shouldWarn = config.warnNoEscapeTeleport()
			&& client.getGameState() == GameState.LOGGED_IN
			&& !hasEscapeItem();

		if (shouldWarn && infoBox == null)
		{
			show();
		}
		else if (!shouldWarn && infoBox != null)
		{
			hide();
		}
	}

	private boolean hasEscapeItem()
	{
		return containsEscapeItem(client.getItemContainer(InventoryID.INV))
			|| containsEscapeItem(client.getItemContainer(InventoryID.WORN));
	}

	private boolean containsEscapeItem(ItemContainer container)
	{
		if (container == null)
		{
			return false;
		}
		for (Item item : container.getItems())
		{
			if (item.getId() > 0
				&& EscapeTeleport.isEnabledEscapeItem(itemManager.getItemComposition(item.getId()).getName(), config))
			{
				return true;
			}
		}
		return false;
	}

	private void show()
	{
		infoBox = new NoEscapeInfoBox(itemManager.getImage(ItemID.AMULET_OF_GLORY_6), plugin);
		infoBoxManager.addInfoBox(infoBox);

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.CONSOLE)
			.runeLiteFormattedMessage(new ChatMessageBuilder()
				.append(Color.RED, "Wilderness Safeguard: you have no teleport that works past level 20.")
				.build())
			.build());
	}

	private void hide()
	{
		if (infoBox != null)
		{
			infoBoxManager.removeInfoBox(infoBox);
			infoBox = null;
		}
	}

	private static class NoEscapeInfoBox extends InfoBox
	{
		NoEscapeInfoBox(BufferedImage image, Plugin plugin)
		{
			super(image, plugin);
			setPriority(InfoBoxPriority.HIGH);
			setTooltip("No escape teleport<br>You have no Ring of wealth, Amulet of glory or Royal seed pod"
				+ "<br>to teleport out between Wilderness levels 21 and 30.");
		}

		@Override
		public String getText()
		{
			return "None";
		}

		@Override
		public Color getTextColor()
		{
			return Color.RED;
		}
	}
}
