package com.hamusuke.flycommod.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFlyingStick extends Item {
	public ItemFlyingStick() {
		super(new Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1));
		this.setRegistryName("flying_stick");
	}

	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent(this.getDescriptionId() + ".desc"));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack item = playerIn.getItemInHand(handIn);

		if (!playerIn.abilities.mayfly) {
			playerIn.abilities.mayfly = true;
			playerIn.onUpdateAbilities();
		} else {
			playerIn.abilities.mayfly = false;
			playerIn.abilities.flying = false;
			playerIn.onUpdateAbilities();
			playerIn.fallDistance = -(float) (playerIn.getY() + 10.0D);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, item);
	}

	public Rarity getRarity(ItemStack stack) {
		return Rarity.EPIC;
	}

	public boolean isFoil(ItemStack p_77636_1_) {
		return true;
	}
}
