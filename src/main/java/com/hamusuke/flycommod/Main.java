package com.hamusuke.flycommod;

import com.hamusuke.flycommod.command.CommandEntityAbilities;
import com.hamusuke.flycommod.command.CommandFlying;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod(Main.MODID)
@EventBusSubscriber
public class Main {
	public static final String MODID = "flycommand";

	@SubscribeEvent
	public static void onServerStarting(final FMLServerStartingEvent event) {
		CommandFlying.register(event.getCommandDispatcher());
		CommandEntityAbilities.register(event.getCommandDispatcher());
	}
}
