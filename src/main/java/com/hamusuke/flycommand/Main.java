package com.hamusuke.flycommand;

import com.hamusuke.flycommand.command.CommandFlying;
import com.hamusuke.flycommand.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Main.MODID, name = Main.MODNAME)
public class Main {
	public static final String MODID = "flycommand";
	public static final String MODNAME = "FlyCommandMod";
	public static final String COMMON = "com.hamusuke.flycommand.proxy.CommonProxy";
	public static final String CLIENT = "com.hamusuke.flycommand.proxy.ClientProxy";

	@SidedProxy(clientSide = CLIENT, serverSide = COMMON)
	public static CommonProxy proxy;

	@EventHandler
	public static void serverInit(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandFlying());
	}
}
