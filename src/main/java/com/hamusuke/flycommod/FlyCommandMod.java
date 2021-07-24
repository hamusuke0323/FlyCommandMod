package com.hamusuke.flycommod;

import com.hamusuke.flycommod.command.EntityAbilitiesCommand;
import com.hamusuke.flycommod.command.FlyCommand;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod(FlyCommandMod.MOD_ID)
@EventBusSubscriber
public class FlyCommandMod {
	public static final String MOD_ID = "flycommand";

	@SubscribeEvent
	public static void onRegisterCommands(final RegisterCommandsEvent event) {
		FlyCommand.register(event.getDispatcher());
		EntityAbilitiesCommand.register(event.getDispatcher());
	}
}
