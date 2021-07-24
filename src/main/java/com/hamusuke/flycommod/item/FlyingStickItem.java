package com.hamusuke.flycommod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class FlyingStickItem extends Item {
	public FlyingStickItem() {
		super(new Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1));
		this.setRegistryName("flying_stick");
	}

	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent(this.getDescriptionId() + ".desc"));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if (!playerIn.getAbilities().mayfly) {
			playerIn.getAbilities().mayfly = true;
			playerIn.onUpdateAbilities();
		} else {
			playerIn.getAbilities().mayfly = false;
			playerIn.getAbilities().flying = false;
			playerIn.onUpdateAbilities();
			playerIn.fallDistance = -(float) (playerIn.getY() + 10.0D);
		}

		return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
	}

	public Rarity getRarity(ItemStack stack) {
		return Rarity.EPIC;
	}

	public boolean isFoil(ItemStack stack) {
		return true;
	}
}
