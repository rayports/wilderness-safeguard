package com.wildernesssafeguard.itemwarnings;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;

class RiskyItemInfoBox extends InfoBox
{
	RiskyItemInfoBox(BufferedImage image, Plugin plugin, RiskyItem item)
	{
		super(image, plugin);
		setPriority(InfoBoxPriority.HIGH);
		setTooltip("Bank your " + item.getDisplayName() + "<br>Wilderness teleports are blocked while you carry it.");
	}

	@Override
	public String getText()
	{
		return "Bank";
	}

	@Override
	public Color getTextColor()
	{
		return Color.RED;
	}
}
