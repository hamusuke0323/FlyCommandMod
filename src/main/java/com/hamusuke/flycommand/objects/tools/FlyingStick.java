package com.hamusuke.flycommand.objects.tools;

import com.hamusuke.flycommand.objects.items.ItemBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nullable;
import java.util.List;

public class FlyingStick extends ItemBase {
	public FlyingStick(String name) {
		super(name);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack item = playerIn.getHeldItem(handIn);

		if (!playerIn.capabilities.allowFlying) {
			playerIn.capabilities.allowFlying = true;
			playerIn.sendPlayerAbilities();
		} else {
			playerIn.capabilities.allowFlying = false;
			playerIn.capabilities.isFlying = false;
			playerIn.sendPlayerAbilities();
			playerIn.fallDistance = -(float) (playerIn.posY + 10.0D);
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, item);
	}

	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format(this.getUnlocalizedName() + ".desc"));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	public IRarity getForgeRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}

	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}
