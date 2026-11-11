package com.wildernesssafeguard;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class WildernessSafeguardPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(WildernessSafeguardPlugin.class);
		RuneLite.main(args);
	}
}