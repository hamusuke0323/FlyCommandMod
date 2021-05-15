package com.hamusuke.flycommand.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class AllowFlyStick extends Item {
	public AllowFlyStick() {
		this.setUnlocalizedName("allowflyingstick");
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setTextureName("flycommand:flying_stick");
		this.setMaxStackSize(1);
	}

	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		p_77624_3_.add(StatCollector.translateToLocalFormatted(this.getUnlocalizedName() + ".desc"));
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
	}

	public ItemStack onItemRightClick(ItemStack item, World worldIn, EntityPlayer playerIn) {
		if (!playerIn.capabilities.allowFlying) {
			playerIn.capabilities.allowFlying = true;
			playerIn.sendPlayerAbilities();
		} else {
			playerIn.capabilities.allowFlying = false;
			playerIn.capabilities.isFlying = false;
			playerIn.sendPlayerAbilities();
			playerIn.fallDistance = -(float) (playerIn.posY + 10.0D);
		}

		return item;
	}

	public EnumRarity getRarity(ItemStack p_77613_1_) {
		return EnumRarity.epic;
	}

	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return true;
	}
}
