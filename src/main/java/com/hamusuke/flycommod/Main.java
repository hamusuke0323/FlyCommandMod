package com.hamusuke.flycommod;

import com.hamusuke.flycommod.command.CommandEntityAbilities;
import com.hamusuke.flycommod.command.CommandFlying;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod(Main.MODID)
@EventBusSubscriber
public class Main {
	public static final String MODID = "flycommand";

	@SubscribeEvent
	public static void onRegisterCommands(final RegisterCommandsEvent event) {
		CommandFlying.register(event.getDispatcher());
		CommandEntityAbilities.register(event.getDispatcher());
	}
}
