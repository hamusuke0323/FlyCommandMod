package com.hamusuke.flycommand.command;

import com.hamusuke.flycommand.FlyCommandMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.Collections;

public class FlyCommand {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("fly").requires(permission -> {
			return permission.hasPermission(2);
		}).executes(FlyCommand::toggle).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("allow").executes(e -> {
			return allow(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> !serverPlayer.getAbilities().mayfly).toList());
		}))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("noAllow").executes(e -> {
			return disallow(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> serverPlayer.getAbilities().mayfly).toList());
		}))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("noGravity").executes(e -> {
			return noGravity(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> !serverPlayer.isNoGravity()).toList());
		}))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("gravity").executes(e -> {
			return gravity(e, EntityArgument.getPlayers(e, "targets").stream().filter(Entity::isNoGravity).toList());
		}))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("getFlySpeed").executes(e -> {
			return getFlySpeed(e, EntityArgument.getPlayers(e, "targets"));
		}))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("getWalkSpeed").executes(e -> {
			return getWalkSpeed(e, EntityArgument.getPlayers(e, "targets"));
		}))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("setFlySpeed").then(Commands.argument("speed", FloatArgumentType.floatArg()).executes(e -> {
			float speed = FloatArgumentType.getFloat(e, "speed");
			return setFlySpeed(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> serverPlayer.getAbilities().getFlyingSpeed() != speed).toList(), speed);
		})))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("setWalkSpeed").then(Commands.argument("speed", FloatArgumentType.floatArg()).executes(e -> {
			float speed = FloatArgumentType.getFloat(e, "speed");
			return setWalkSpeed(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> serverPlayer.getAbilities().getWalkingSpeed() != speed).toList(), speed);
		})))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("allowEdit").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return allowEdit(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> serverPlayer.getAbilities().mayBuild != flag).toList(), flag);
		})))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("disableDamage").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return disableDamage(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> serverPlayer.getAbilities().invulnerable != flag).toList(), flag);
		})))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("isFlying").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return isFlying(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> serverPlayer.getAbilities().flying != flag).toList(), flag);
		})))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("setGlowing").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return isGlowing(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> serverPlayer.hasGlowingTag() != flag).toList(), flag);
		})))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("setInvulnerable").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return isInvulnerable(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> serverPlayer.isInvulnerable() != flag).toList(), flag);
		})))).then(Commands.argument("targets", EntityArgument.players()).then(Commands.literal("setInvisible").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(e -> {
			boolean flag = BoolArgumentType.getBool(e, "boolean");
			return isInvisible(e, EntityArgument.getPlayers(e, "targets").stream().filter(serverPlayer -> serverPlayer.isInvisible() != flag).toList(), flag);
		})))));
	}

	private static int toggle(CommandContext<CommandSourceStack> source) throws CommandSyntaxException {
		ServerPlayer serverPlayer = source.getSource().getPlayerOrException();
		return serverPlayer.getAbilities().mayfly ? disallow(source, Collections.singleton(serverPlayer)) : allow(source, Collections.singleton(serverPlayer));
	}

	private static int allow(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players) {
		players.forEach(serverPlayer -> {
			serverPlayer.getAbilities().mayfly = true;
			serverPlayer.onUpdateAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.allow.single", players.iterator().next().getDisplayName())), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.allow.multiple", players.size())), true);
		}

		return players.size();
	}

	private static int disallow(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players) {
		players.forEach(serverPlayer -> {
			serverPlayer.getAbilities().mayfly = false;
			serverPlayer.getAbilities().flying = false;
			serverPlayer.onUpdateAbilities();
			FlyCommandMod.mark(serverPlayer, true);
		});

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.noallow.single", players.iterator().next().getDisplayName())), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.noallow.multiple", players.size())), true);
		}

		return players.size();
	}

	private static int noGravity(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players) {
		players.forEach(serverPlayer -> serverPlayer.setNoGravity(true));

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.nogravity.single", players.iterator().next().getDisplayName())), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.nogravity.multiple", players.size())), true);
		}

		return players.size();
	}

	private static int gravity(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players) {
		players.forEach(serverPlayer -> {
			serverPlayer.setNoGravity(false);
			FlyCommandMod.mark(serverPlayer, true);
		});

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.gravity.single", players.iterator().next().getDisplayName())), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.gravity.multiple", players.size())), true);
		}

		return players.size();
	}

	private static int getFlySpeed(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players) {
		players.forEach(serverPlayer -> source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.getflyspeed", serverPlayer.getDisplayName(), serverPlayer.getAbilities().getFlyingSpeed())), false));
		return players.size();
	}

	private static int getWalkSpeed(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players) {
		players.forEach(serverPlayer -> source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.getwalkspeed", serverPlayer.getDisplayName(), serverPlayer.getAbilities().getWalkingSpeed())), false));
		return players.size();
	}

	private static int setFlySpeed(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players, float flySpeed) {
		players.forEach(serverPlayer -> {
			serverPlayer.getAbilities().setFlyingSpeed(flySpeed);
			serverPlayer.onUpdateAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.setflyspeed.single", players.iterator().next().getDisplayName(), flySpeed)), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.setflyspeed.multiple", players.size(), flySpeed)), true);
		}

		return players.size();
	}

	private static int setWalkSpeed(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players, float walkSpeed) {
		players.forEach(serverPlayer -> {
			serverPlayer.getAbilities().setWalkingSpeed(walkSpeed);
			serverPlayer.onUpdateAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.setwalkspeed.single", players.iterator().next().getDisplayName(), walkSpeed)), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.setwalkspeed.multiple", players.size(), walkSpeed)), true);
		}

		return players.size();
	}

	private static int allowEdit(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players, boolean flag) {
		players.forEach(serverPlayer -> {
			serverPlayer.getAbilities().mayBuild = flag;
			serverPlayer.onUpdateAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.allowedit." + flag + ".single", players.iterator().next().getDisplayName())), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.allowedit." + flag + ".multiple", players.size())), true);
		}

		return players.size();
	}

	private static int disableDamage(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players, boolean flag) {
		players.forEach(serverPlayer -> {
			serverPlayer.getAbilities().invulnerable = flag;
			serverPlayer.onUpdateAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.disabledamage." + flag + ".single", players.iterator().next().getDisplayName())), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.disabledamage." + flag + ".multiple", players.size())), true);
		}

		return players.size();
	}

	private static int isFlying(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players, boolean flag) {
		players.forEach(serverPlayer -> {
			serverPlayer.getAbilities().flying = flag;
			serverPlayer.onUpdateAbilities();
		});

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.isflying." + flag + ".single", players.iterator().next().getDisplayName())), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.isflying." + flag + ".multiple", players.size())), true);
		}

		return players.size();
	}

	private static int isGlowing(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players, boolean flag) {
		players.forEach(serverPlayer -> serverPlayer.setGlowingTag(flag));

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.setglowing." + flag + ".single", players.iterator().next().getDisplayName())), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.setglowing." + flag + ".multiple", players.size())), true);
		}

		return players.size();
	}

	private static int isInvulnerable(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players, boolean flag) {
		players.forEach(serverPlayer -> serverPlayer.setInvulnerable(flag));

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.setinvulnerable." + flag + ".single", players.iterator().next().getDisplayName())), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.setinvulnerable." + flag + ".multiple", players.size())), true);
		}

		return players.size();
	}

	private static int isInvisible(CommandContext<CommandSourceStack> source, Collection<ServerPlayer> players, boolean flag) {
		players.forEach(serverPlayer -> serverPlayer.setInvisible(flag));

		if (players.size() == 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.invisible." + flag + ".single", players.iterator().next().getDisplayName())), true);
		} else if (players.size() > 1) {
			source.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("hamusuke.command.fly.success.invisible." + flag + ".multiple", players.size())), true);
		}

		return players.size();
	}
}
