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
		super(new Properties().group(ItemGroup.TOOLS).maxStackSize(1));
		this.setRegistryName("flying_stick");
	}

	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc"));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack item = playerIn.getHeldItem(handIn);

		if (!playerIn.abilities.allowFlying) {
			playerIn.abilities.allowFlying = true;
			playerIn.sendPlayerAbilities();
		} else {
			playerIn.abilities.allowFlying = false;
			playerIn.abilities.isFlying = false;
			playerIn.sendPlayerAbilities();
			playerIn.fallDistance = -(float) (playerIn.getPosY() + 10.0D);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, item);
	}

	public Rarity getRarity(ItemStack stack) {
		return Rarity.EPIC;
	}

	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}
