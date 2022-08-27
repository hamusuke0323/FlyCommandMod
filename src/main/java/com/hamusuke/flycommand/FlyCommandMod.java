package com.hamusuke.flycommand;

import com.hamusuke.flycommand.command.EntityAbilitiesCommand;
import com.hamusuke.flycommand.command.FlyCommand;
import com.hamusuke.flycommand.invoker.LivingEntityInvoker;
import com.hamusuke.flycommand.item.FlyingStickItem;
import com.hamusuke.flycommand.network.Network;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(FlyCommandMod.MOD_ID)
public class FlyCommandMod {
	public static final String MOD_ID = "flycommand";

	public FlyCommandMod() {
		ModRegistries.init();
		Network.load();
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

	static final class ModRegistries {
		private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FlyCommandMod.MOD_ID);
		public static final RegistryObject<FlyingStickItem> FLYING_STICK_ITEM = ITEMS.register("flying_stick", FlyingStickItem::new);

		static void init() {
			IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
			ITEMS.register(bus);
		}
	}
}
