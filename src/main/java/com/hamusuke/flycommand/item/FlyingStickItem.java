package com.hamusuke.flycommand.item;

import com.hamusuke.flycommand.FlyCommandMod;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FlyingStickItem extends Item {
	public FlyingStickItem(Properties p_41383_) {
		super(p_41383_);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(MutableComponent.create(new TranslatableContents(this.getDescriptionId() + ".desc")));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if (!playerIn.getAbilities().mayfly) {
			playerIn.getAbilities().mayfly = true;
			playerIn.onUpdateAbilities();
		} else {
			playerIn.getAbilities().mayfly = false;
			playerIn.getAbilities().flying = false;
			playerIn.onUpdateAbilities();
			FlyCommandMod.mark(playerIn, true);
		}

		return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return Rarity.EPIC;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
}
