package com.hamusuke.flycommand.command;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandFlying extends CommandBase {
	public String getCommandName() {
		return "fly";
	}

	public int getRequiredPermissionLevel() {
		return 2;
	}

	public String getCommandUsage(ICommandSender sender) {
		return "hamusuke.command.fly.usage";
	}

	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length <= 1) {
			throw new WrongUsageException("hamusuke.command.fly.usage");
		} else {
			EntityPlayerMP entityplayermp = getPlayer(sender, args[0]);

			if (args[1].equalsIgnoreCase("allow")) {
				entityplayermp.capabilities.allowFlying = true;
				entityplayermp.sendPlayerAbilities();

				if (entityplayermp == sender) {
					func_152373_a(sender, this, "hamusuke.command.fly.success.allow.self");
				} else {
					func_152374_a(sender, this, 1, "hamusuke.command.fly.success.allow.other", entityplayermp.getCommandSenderName());
				}
			} else if (args[1].equalsIgnoreCase("noallow")) {
				entityplayermp.capabilities.allowFlying = false;
				entityplayermp.capabilities.isFlying = false;
				entityplayermp.sendPlayerAbilities();

				if (entityplayermp == sender) {
					func_152373_a(sender, this, "hamusuke.command.fly.success.noallow.self");
				} else {
					func_152374_a(sender, this, 1, "hamusuke.command.fly.success.noallow.other", entityplayermp.getCommandSenderName());
				}
			} else if (args[1].equalsIgnoreCase("setflyspeed")) {
				try {
					NBTTagCompound nbtTagCompound = new NBTTagCompound();
					entityplayermp.capabilities.writeCapabilitiesToNBT(nbtTagCompound);
					NBTTagCompound nbttagcompound1 = nbtTagCompound.getCompoundTag("abilities");
					nbttagcompound1.setFloat("flySpeed", Float.parseFloat(args[2]));
					entityplayermp.capabilities.readCapabilitiesFromNBT(nbtTagCompound);
					entityplayermp.sendPlayerAbilities();

					if (entityplayermp == sender) {
						func_152373_a(sender, this, "hamusuke.command.fly.success.setflyspeed.self", args[2]);
					} else {
						func_152374_a(sender, this, 1, "hamusuke.command.fly.success.setflyspeed.other", entityplayermp.getCommandSenderName(), args[2]);
					}
				} catch (NumberFormatException e) {
					if (entityplayermp == sender) {
						func_152373_a(sender, this, "hamusuke.command.fly.failed.setflyspeed");
					} else {
						func_152374_a(sender, this, 1, "hamusuke.command.fly.failed.setflyspeed");
					}
				}
			} else if (args[1].equalsIgnoreCase("setwalkspeed")) {
				try {
					NBTTagCompound nbtTagCompound = new NBTTagCompound();
					entityplayermp.capabilities.writeCapabilitiesToNBT(nbtTagCompound);
					NBTTagCompound nbttagcompound1 = nbtTagCompound.getCompoundTag("abilities");
					nbttagcompound1.setFloat("walkSpeed", Float.parseFloat(args[2]));
					entityplayermp.capabilities.readCapabilitiesFromNBT(nbtTagCompound);
					entityplayermp.sendPlayerAbilities();

					if (entityplayermp == sender) {
						func_152373_a(sender, this, "hamusuke.command.fly.success.setwalkspeed.self", args[2]);
					} else {
						func_152374_a(sender, this, 1, "hamusuke.command.fly.success.setwalkspeed.other", entityplayermp.getCommandSenderName(), args[2]);
					}
				} catch (NumberFormatException e) {
					if (entityplayermp == sender) {
						func_152373_a(sender, this, "hamusuke.command.fly.failed.setwalkspeed");
					} else {
						func_152374_a(sender, this, 1, "hamusuke.command.fly.failed.setwalkspeed");
					}
				}
			} else if (args[1].equalsIgnoreCase("allowedit")) {
				if (args[2].equalsIgnoreCase("true")) {
					entityplayermp.capabilities.allowEdit = true;
					entityplayermp.sendPlayerAbilities();

					if (entityplayermp == sender) {
						func_152373_a(sender, this, "hamusuke.command.fly.success.allowedit.true.self");
					} else {
						func_152374_a(sender, this, 1, "hamusuke.command.fly.success.allowedit.true.other", entityplayermp.getCommandSenderName());
					}
				} else if (args[2].equalsIgnoreCase("false")) {
					entityplayermp.capabilities.allowEdit = false;
					entityplayermp.sendPlayerAbilities();

					if (entityplayermp == sender) {
						func_152373_a(sender, this, "hamusuke.command.fly.success.allowedit.false.self");
					} else {
						func_152374_a(sender, this, 1, "hamusuke.command.fly.success.allowedit.false.other", entityplayermp.getCommandSenderName());
					}
				} else {
					func_152373_a(sender, this, "hamusuke.command.fly.error.boolean");
				}
			} else if (args[1].equalsIgnoreCase("disabledamage")) {
				if (args[2].equalsIgnoreCase("true")) {
					entityplayermp.capabilities.disableDamage = true;
					entityplayermp.sendPlayerAbilities();

					if (entityplayermp == sender) {
						func_152373_a(sender, this, "hamusuke.command.fly.success.disabledamage.true.self");
					} else {
						func_152374_a(sender, this, 1, "hamusuke.command.fly.success.disabledamage.true.other", entityplayermp.getCommandSenderName());
					}
				} else if (args[2].equalsIgnoreCase("false")) {
					entityplayermp.capabilities.disableDamage = false;
					entityplayermp.sendPlayerAbilities();

					if (entityplayermp == sender) {
						func_152373_a(sender, this, "hamusuke.command.fly.success.disabledamage.false.self");
					} else {
						func_152374_a(sender, this, 1, "hamusuke.command.fly.success.disabledamage.false.other", entityplayermp.getCommandSenderName());
					}
				} else {
					func_152373_a(sender, this, "hamusuke.command.fly.error.boolean");
				}
			} else if (args[1].equalsIgnoreCase("isflying")) {
				if (args[2].equalsIgnoreCase("true")) {
					entityplayermp.capabilities.isFlying = true;
					entityplayermp.sendPlayerAbilities();

					if (entityplayermp == sender) {
						func_152373_a(sender, this, "hamusuke.command.fly.success.isflying.true.self");
					} else {
						func_152374_a(sender, this, 1, "hamusuke.command.fly.success.isflying.true.other", entityplayermp.getCommandSenderName());
					}
				} else if (args[2].equalsIgnoreCase("false")) {
					entityplayermp.capabilities.isFlying = false;
					entityplayermp.sendPlayerAbilities();

					if (entityplayermp == sender) {
						func_152373_a(sender, this, "hamusuke.command.fly.success.isflying.false.self");
					} else {
						func_152374_a(sender, this, 1, "hamusuke.command.fly.success.isflying.false.other", entityplayermp.getCommandSenderName());
					}
				} else {
					func_152373_a(sender, this, "hamusuke.command.fly.error.boolean");
				}
			} else if (args[1].equalsIgnoreCase("getflyspeed")) {
				if (entityplayermp == sender) {
					func_152373_a(sender, this, "hamusuke.command.fly.success.getflyspeed.self", entityplayermp.capabilities.getFlySpeed());
				} else {
					func_152374_a(sender, this, 1, "hamusuke.command.fly.success.getflyspeed.other", entityplayermp.getCommandSenderName(), entityplayermp.capabilities.getFlySpeed());
				}
			} else if (args[1].equalsIgnoreCase("getwalkspeed")) {
				if (entityplayermp == sender) {
					func_152373_a(sender, this, "hamusuke.command.fly.success.getwalkspeed.self", entityplayermp.capabilities.getWalkSpeed());
				} else {
					func_152374_a(sender, this, 1, "hamusuke.command.fly.success.getwalkspeed.other", entityplayermp.getCommandSenderName(), entityplayermp.capabilities.getWalkSpeed());
				}
			}
		}
	}

	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, this.getListOfPlayerUsernames());
		} else if (args.length == 2) {
			return getListOfStringsMatchingLastWord(args, "allow", "noallow", "setflyspeed", "setwalkspeed", "allowedit", "disabledamage", "isflying", "getflyspeed", "getwalkspeed");
		} else {
			return args[1].equalsIgnoreCase("allowedit") || args[1].equalsIgnoreCase("disabledamage") || args[1].equalsIgnoreCase("isflying") ? getListOfStringsMatchingLastWord(args, "true", "false") : Collections.emptyList();
		}
	}

	protected String[] getListOfPlayerUsernames() {
		return MinecraftServer.getServer().getAllUsernames();
	}

	public boolean isUsernameIndex(String[] args, int index) {
		return index == 1;
	}
}