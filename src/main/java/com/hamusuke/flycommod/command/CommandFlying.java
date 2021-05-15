package com.hamusuke.flycommod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.stream.Collectors;

public class CommandFlying {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> literalArgumentBuilder = Commands.literal("fly").requires((permission) -> permission.hasPermissionLevel(2));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("allow").executes(e -> allow(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> !serverPlayerEntity.abilities.allowFlying).collect(Collectors.toList())))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("noAllow").executes(e -> disallow(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.abilities.allowFlying).collect(Collectors.toList())))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("noGravity").executes(e -> noGravity(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> !serverPlayerEntity.hasNoGravity()).collect(Collectors.toList())))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("gravity").executes(e -> gravity(e, EntityArgument.getPlayers(e, "targets").stream().filter(Entity::hasNoGravity).collect(Collectors.toList())))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("getFlySpeed").executes(e -> getFlySpeed(e, EntityArgument.getPlayers(e, "targets")))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("getWalkSpeed").executes(e -> getWalkSpeed(e, EntityArgument.getPlayers(e, "targets")))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("setFlySpeed").then(Commands.argument("speed", FloatArgumentType.floatArg()).executes(e -> {
			float speed = FloatArgumentType.getFloat(e, "speed");
			return setFlySpeed(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.abilities.getFlySpeed() != speed).collect(Collectors.toList()), speed);
		}))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("setWalkSpeed").then(Commands.argument("speed", FloatArgumentType.floatArg()).executes(e -> {
			float speed = FloatArgumentType.getFloat(e, "speed");
			return setWalkSpeed(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.abilities.getWalkSpeed() != speed).collect(Collectors.toList()), speed);
		}))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("allowEdit").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return allowEdit(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.abilities.allowEdit != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("disableDamage").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return disableDamage(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.abilities.disableDamage != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("isFlying").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return isFlying(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.abilities.isFlying != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("setGlowing").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return isGlowing(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.isGlowing() != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("setInvulnerable").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return isInvulnerable(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.isInvulnerable() != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("setInvisible").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return isInvisible(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayerEntity -> serverPlayerEntity.isInvisible() != flag).collect(Collectors.toList()), flag);
		}))));

		dispatcher.register(literalArgumentBuilder);
	}

	private static int allow(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.abilities.allowFlying = true;
			serverPlayerEntity.sendPlayerAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.allow.single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.allow.multiple", players.size()), true);
		}

		return players.size();
	}

	private static int disallow(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.abilities.allowFlying = false;
			serverPlayerEntity.abilities.isFlying = false;
			serverPlayerEntity.sendPlayerAbilities();
			serverPlayerEntity.fallDistance = -(float) (serverPlayerEntity.getPosY() + 10.0D);
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.noallow.single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.noallow.multiple", players.size()), true);
		}

		return players.size();
	}

	private static int noGravity(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> serverPlayerEntity.setNoGravity(true));

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.nogravity.single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.nogravity.multiple", players.size()), true);
		}

		return players.size();
	}

	private static int gravity(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.setNoGravity(false);
			serverPlayerEntity.fallDistance = -(float) (serverPlayerEntity.getPosY() + 10.0D);
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.gravity.single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.gravity.multiple", players.size()), true);
		}

		return players.size();
	}

	private static int getFlySpeed(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.getflyspeed", serverPlayerEntity.getDisplayName(), serverPlayerEntity.abilities.getFlySpeed()), false));
		return players.size();
	}

	private static int getWalkSpeed(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players) {
		players.forEach(serverPlayerEntity -> source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.getwalkspeed", serverPlayerEntity.getDisplayName(), serverPlayerEntity.abilities.getWalkSpeed()), false));
		return players.size();
	}

	private static int setFlySpeed(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players, float flySpeed) {
		players.forEach(serverPlayerEntity -> {
			CompoundNBT compoundNBT = new CompoundNBT();
			serverPlayerEntity.abilities.write(compoundNBT);
			CompoundNBT compoundnbt = compoundNBT.getCompound("abilities");
			compoundnbt.putFloat("flySpeed", flySpeed);
			serverPlayerEntity.abilities.read(compoundNBT);
			serverPlayerEntity.sendPlayerAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.setflyspeed.single", players.iterator().next().getDisplayName(), flySpeed), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.setflyspeed.multiple", players.size(), flySpeed), true);
		}

		return players.size();
	}

	private static int setWalkSpeed(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players, float walkSpeed) {
		players.forEach(serverPlayerEntity -> {
			CompoundNBT compoundNBT = new CompoundNBT();
			serverPlayerEntity.abilities.write(compoundNBT);
			CompoundNBT compoundnbt = compoundNBT.getCompound("abilities");
			compoundnbt.putFloat("walkSpeed", walkSpeed);
			serverPlayerEntity.abilities.read(compoundNBT);
			serverPlayerEntity.sendPlayerAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.setwalkspeed.single", players.iterator().next().getDisplayName(), walkSpeed), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.setwalkspeed.multiple", players.size(), walkSpeed), true);
		}

		return players.size();
	}

	private static int allowEdit(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.abilities.allowEdit = flag;
			serverPlayerEntity.sendPlayerAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.allowedit." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.allowedit." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}

	private static int disableDamage(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.abilities.disableDamage = flag;
			serverPlayerEntity.sendPlayerAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.disabledamage." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.disabledamage." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}

	private static int isFlying(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> {
			serverPlayerEntity.abilities.isFlying = flag;
			serverPlayerEntity.sendPlayerAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.isflying." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.isflying." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}

	private static int isGlowing(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> serverPlayerEntity.setGlowing(flag));

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.setglowing." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.setglowing." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}

	private static int isInvulnerable(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> serverPlayerEntity.setInvulnerable(flag));

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.setinvulnerable." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.setinvulnerable." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}

	private static int isInvisible(CommandContext<CommandSource> source, Collection<ServerPlayerEntity> players, boolean flag) {
		players.forEach(serverPlayerEntity -> serverPlayerEntity.setInvisible(flag));

		if (players.size() == 1) {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.invisible." + flag + ".single", players.iterator().next().getDisplayName()), true);
		} else {
			source.getSource().sendFeedback(new TranslationTextComponent("hamusuke.command.fly.success.invisible." + flag + ".multiple", players.size()), true);
		}

		return players.size();
	}
}
