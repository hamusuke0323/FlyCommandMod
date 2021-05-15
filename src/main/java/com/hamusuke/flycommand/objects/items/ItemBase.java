package com.hamusuke.flycommand.objects.items;

import com.hamusuke.flycommand.Main;
import com.hamusuke.flycommand.init.ItemInit;
import com.hamusuke.flycommand.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
	public ItemBase(String name) {
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.setMaxStackSize(1);

		ItemInit.ITEMS.add(this);
	}

	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
