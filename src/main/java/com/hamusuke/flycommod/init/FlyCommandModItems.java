package com.hamusuke.flycommod.init;

import com.hamusuke.flycommod.Main;
import com.hamusuke.flycommod.item.ItemFlyingStick;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Main.MODID)
public class FlyCommandModItems {
	@Mod.EventBusSubscriber(modid = Main.MODID, bus = Bus.MOD)
	public static class Register {
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final Item[] item = {new ItemFlyingStick()};
			event.getRegistry().registerAll(item);
		}
	}
}
