package com.hamusuke.flycommand.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandFlying extends CommandBase {
	public String getName() {
		return "fly";
	}

	public String getUsage(ICommandSender sender) {
		return "hamusuke.command.fly.usage";
	}

	public int getRequiredPermissionLevel() {
		return 2;
	}

	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		} else if (args.length == 2) {
			return getListOfStringsMatchingLastWord(args, "allow", "noallow", "nogravity", "gravity", "setflyspeed", "setwalkspeed", "allowedit", "disabledamage", "isflying", "getflyspeed", "getwalkspeed");
		} else {
			return args[1].equalsIgnoreCase("allowedit") || args[1].equalsIgnoreCase("disabledamage") || args[1].equalsIgnoreCase("isflying") ? getListOfStringsMatchingLastWord(args, "true", "false") : new ArrayList<String>();
		}
	}

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length <= 1) {
			throw new WrongUsageException("hamusuke.command.fly.usage");
		} else {
			EntityPlayerMP entityplayer = getPlayer(server, sender, args[0]);

			if (args[1].equalsIgnoreCase("allow")) {
				entityplayer.capabilities.allowFlying = true;
				entityplayer.sendPlayerAbilities();

				if (entityplayer == sender) {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.allow.self"));
				} else {
					notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.allow.other", entityplayer.getName());
				}
			} else if (args[1].equalsIgnoreCase("noallow")) {
				entityplayer.capabilities.allowFlying = false;
				entityplayer.capabilities.isFlying = false;
				entityplayer.fallDistance = -(float) (entityplayer.posY + 10.0D);
				entityplayer.sendPlayerAbilities();

				if (entityplayer == sender) {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.noallow.self"));
				} else {
					notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.noallow.other", entityplayer.getName());
				}
			} else if (args[1].equalsIgnoreCase("nogravity")) {
				entityplayer.setNoGravity(true);

				if (entityplayer == sender) {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.nogravity.self"));
				} else {
					notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.nogravity.other", entityplayer.getName());
				}
			} else if (args[1].equalsIgnoreCase("gravity")) {
				entityplayer.setNoGravity(false);
				entityplayer.fallDistance = -(float) (entityplayer.posY + 10.0D);

				if (entityplayer == sender) {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.gravity.self"));
				} else {
					notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.gravity.other", entityplayer.getName());
				}
			} else if (args[1].equalsIgnoreCase("setflyspeed")) {
				try {
					NBTTagCompound nbtTagCompound = new NBTTagCompound();
					entityplayer.capabilities.writeCapabilitiesToNBT(nbtTagCompound);
					NBTTagCompound nbttagcompound1 = nbtTagCompound.getCompoundTag("abilities");
					nbttagcompound1.setFloat("flySpeed", Float.parseFloat(args[2]));
					entityplayer.capabilities.readCapabilitiesFromNBT(nbtTagCompound);
					entityplayer.sendPlayerAbilities();

					if (entityplayer == sender) {
						entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.setflyspeed.self", args[2]));
					} else {
						notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.setflyspeed.other", entityplayer.getName(), args[2]);
					}
				} catch (NumberFormatException e) {
					sender.sendMessage(new TextComponentTranslation("hamusuke.command.fly.failed.setflyspeed"));
				}
			} else if (args[1].equalsIgnoreCase("setwalkspeed")) {
				try {
					NBTTagCompound nbtTagCompound = new NBTTagCompound();
					entityplayer.capabilities.writeCapabilitiesToNBT(nbtTagCompound);
					NBTTagCompound nbttagcompound1 = nbtTagCompound.getCompoundTag("abilities");
					nbttagcompound1.setFloat("walkSpeed", Float.parseFloat(args[2]));
					entityplayer.capabilities.readCapabilitiesFromNBT(nbtTagCompound);
					entityplayer.sendPlayerAbilities();

					if (entityplayer == sender) {
						entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.setwalkspeed.self", args[2]));
					} else {
						notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.setwalkspeed.other", entityplayer.getName(), args[2]);
					}
				} catch (NumberFormatException e) {
					sender.sendMessage(new TextComponentTranslation("hamusuke.command.fly.failed.setwalkspeed"));
				}
			} else if (args[1].equalsIgnoreCase("allowedit")) {
				if (args[2].equalsIgnoreCase("true")) {
					entityplayer.capabilities.allowEdit = true;
					entityplayer.sendPlayerAbilities();

					if (entityplayer == sender) {
						entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.allowedit.true.self"));
					} else {
						notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.allowedit.true.other", entityplayer.getName());
					}
				} else if (args[2].equalsIgnoreCase("false")) {
					entityplayer.capabilities.allowEdit = false;
					entityplayer.sendPlayerAbilities();

					if (entityplayer == sender) {
						entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.allowedit.false.self"));
					} else {
						notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.allowedit.false.other", entityplayer.getName());
					}
				} else {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.error.boolean"));
				}
			} else if (args[1].equalsIgnoreCase("disabledamage")) {
				if (args[2].equalsIgnoreCase("true")) {
					entityplayer.capabilities.disableDamage = true;
					entityplayer.sendPlayerAbilities();

					if (entityplayer == sender) {
						entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.disabledamage.true.self"));
					} else {
						notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.disabledamage.true.other", entityplayer.getName());
					}
				} else if (args[2].equalsIgnoreCase("false")) {
					entityplayer.capabilities.disableDamage = false;
					entityplayer.sendPlayerAbilities();

					if (entityplayer == sender) {
						entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.disabledamage.false.self"));
					} else {
						notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.disabledamage.false.other", entityplayer.getName());
					}
				} else {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.error.boolean"));
				}
			} else if (args[1].equalsIgnoreCase("isflying")) {
				if (args[2].equalsIgnoreCase("true")) {
					entityplayer.capabilities.isFlying = true;
					entityplayer.sendPlayerAbilities();

					if (entityplayer == sender) {
						entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.isflying.true.self"));
					} else {
						notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.isflying.true.other", entityplayer.getName());
					}
				} else if (args[2].equalsIgnoreCase("false")) {
					entityplayer.capabilities.isFlying = false;
					entityplayer.sendPlayerAbilities();

					if (entityplayer == sender) {
						entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.isflying.false.self"));
					} else {
						notifyCommandListener(sender, this, 1, "hamusuke.command.fly.success.isflying.false.other", entityplayer.getName());
					}
				} else {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.error.boolean"));
				}
			} else if (args[1].equalsIgnoreCase("getflyspeed")) {
				if (entityplayer == sender) {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.getflyspeed.self", entityplayer.capabilities.getFlySpeed()));
				} else {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.getflyspeed.other", entityplayer.getName(), entityplayer.capabilities.getFlySpeed()));
				}
			} else if (args[1].equalsIgnoreCase("getwalkspeed")) {
				if (entityplayer == sender) {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.getwalkspeed.self", entityplayer.capabilities.getWalkSpeed()));
				} else {
					entityplayer.sendMessage(new TextComponentTranslation("hamusuke.command.fly.success.getwalkspeed.other", entityplayer.getName(), entityplayer.capabilities.getWalkSpeed()));
				}
			}
		}
	}
}
