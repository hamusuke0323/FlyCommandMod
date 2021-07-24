package com.hamusuke.flycommod.init;

import com.hamusuke.flycommod.FlyCommandMod;
import com.hamusuke.flycommod.item.FlyingStickItem;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(FlyCommandMod.MOD_ID)
public class FlyCommandModItems {
	@Mod.EventBusSubscriber(modid = FlyCommandMod.MOD_ID, bus = Bus.MOD)
	public static class Register {
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			event.getRegistry().register(new FlyingStickItem());
		}
	}
}
