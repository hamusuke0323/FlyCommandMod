package com.hamusuke.flycommand;

import com.hamusuke.flycommand.command.CommandFlying;
import com.hamusuke.flycommand.items.AllowFlyStick;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
public class Main {
	public static final String MODID = "flycommand";
	public static final String MODNAME = "FlyCommandMod";
	public static final String VERSION = "1.0";

	public static final Item allowFlyStick = new AllowFlyStick();

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerItem(allowFlyStick, "AllowFlyingStick");
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		GameRegistry.addRecipe(new ItemStack(allowFlyStick), "ZXZ", "YXY", " X ", 'X', Items.stick, 'Y', Items.blaze_rod, 'Z', Items.feather);
	}

	@EventHandler
	public static void serverInit(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandFlying());
	}
}
