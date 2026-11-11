package com.wildernesssafeguard;

import com.google.inject.Provides;
import com.wildernesssafeguard.escape.EscapeTeleportWarning;
import com.wildernesssafeguard.itemwarnings.ItemWarningTracker;
import com.wildernesssafeguard.teleport.WildernessTeleportBlocker;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Wilderness Safeguard",
	description = "A collection of tools to make the Wilderness safer",
	tags = {"wilderness", "pvp", "high risk", "teleport"}
)
public class WildernessSafeguardPlugin extends Plugin
{
	@Inject
	private EventBus eventBus;

	@Inject
	private WildernessTeleportBlocker teleportBlocker;

	@Inject
	private ItemWarningTracker itemWarningTracker;

	@Inject
	private EscapeTeleportWarning escapeTeleportWarning;

	@Override
	protected void startUp()
	{
		eventBus.register(itemWarningTracker);
		eventBus.register(teleportBlocker);
		itemWarningTracker.startUp(this);
		escapeTeleportWarning.startUp(this);
		eventBus.register(escapeTeleportWarning);
	}

	@Override
	protected void shutDown()
	{
		eventBus.unregister(teleportBlocker);
		eventBus.unregister(itemWarningTracker);
		itemWarningTracker.shutDown();
		eventBus.unregister(escapeTeleportWarning);
		escapeTeleportWarning.shutDown();
	}

	@Provides
	WildernessSafeguardConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WildernessSafeguardConfig.class);
	}
}
