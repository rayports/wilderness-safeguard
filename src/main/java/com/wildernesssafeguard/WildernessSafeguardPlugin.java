package com.wildernesssafeguard;

import com.google.inject.Provides;
import com.wildernesssafeguard.highrisk.HighRiskTeleportBlocker;
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
	private HighRiskTeleportBlocker highRiskTeleportBlocker;

	@Override
	protected void startUp()
	{
		eventBus.register(highRiskTeleportBlocker);
	}

	@Override
	protected void shutDown()
	{
		eventBus.unregister(highRiskTeleportBlocker);
	}

	@Provides
	WildernessSafeguardConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WildernessSafeguardConfig.class);
	}
}
