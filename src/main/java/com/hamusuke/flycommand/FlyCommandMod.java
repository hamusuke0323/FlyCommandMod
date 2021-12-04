package com.hamusuke.flycommand;

import com.hamusuke.flycommand.command.EntityAbilitiesCommand;
import com.hamusuke.flycommand.command.FlyCommand;

import com.hamusuke.flycommand.invoker.LivingEntityInvoker;
import com.hamusuke.flycommand.item.FlyingStickItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod(FlyCommandMod.MOD_ID)
@Mod.EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class FlyCommandMod {
	public static final String MOD_ID = "flycommand";
	public static final Item FLYING_STICK = new FlyingStickItem();

	public FlyCommandMod() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onFall(final LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		if (isMarked(livingEntity)) {
			mark(livingEntity, false);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onChangedDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
		event.getPlayer().onUpdateAbilities();
	}

	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
		event.getRegistry().register(FLYING_STICK);
	}

	@SubscribeEvent
	public void onRegisterCommands(final RegisterCommandsEvent event) {
		FlyCommand.register(event.getDispatcher());
		EntityAbilitiesCommand.register(event.getDispatcher());
	}

	public static void mark(LivingEntity livingEntity, boolean flag) {
		((LivingEntityInvoker) livingEntity).mark(flag);
	}

	public static boolean isMarked(LivingEntity livingEntity) {
		return ((LivingEntityInvoker) livingEntity).isMarked();
	}
}
