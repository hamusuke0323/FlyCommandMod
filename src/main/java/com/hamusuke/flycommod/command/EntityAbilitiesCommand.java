package com.hamusuke.flycommod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;

import java.util.Collection;

public class EntityAbilitiesCommand {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("entityabilities").requires((permission) -> {
			return permission.hasPermission(2);
		}).then(Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("noGravity").then(Commands.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return noGravity(command.getSource(), EntityArgument.getEntities(command, "targets").stream().filter((entity) -> entity.isNoGravity() != flag).toList(), flag);
		})))).then(Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("setGlowing").then(Commands.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setGlowing(command.getSource(), EntityArgument.getEntities(command, "targets").stream().filter((entity) -> entity.hasGlowingTag() != flag).toList(), flag);
		})))).then(Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("setInvulnerable").then(Commands.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setInvulnerable(command.getSource(), EntityArgument.getEntities(command, "targets").stream().filter((entity) -> entity.isInvulnerable() != flag).toList(), flag);
		})))).then(Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("setInvisible").then(Commands.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setInvisible(command.getSource(), EntityArgument.getEntities(command, "targets").stream().filter((entity) -> entity.isInvisible() != flag).toList(), flag);
		})))));
	}

	private static int noGravity(CommandSourceStack source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> {
			entity.setNoGravity(flag);
			if (!flag) {
				entity.fallDistance = -(float) (entity.getY() + 10.0D);
			}
		});

		if (entities.size() == 1) {
			source.sendSuccess(new TranslatableComponent("hamusuke.command.entityabilities.success.nogravity." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendSuccess(new TranslatableComponent("hamusuke.command.entityabilities.success.nogravity." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setGlowing(CommandSourceStack source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setGlowingTag(flag));

		if (entities.size() == 1) {
			source.sendSuccess(new TranslatableComponent("hamusuke.command.entityabilities.success.setglowing." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendSuccess(new TranslatableComponent("hamusuke.command.entityabilities.success.setglowing." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setInvulnerable(CommandSourceStack source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setInvulnerable(flag));

		if (entities.size() == 1) {
			source.sendSuccess(new TranslatableComponent("hamusuke.command.entityabilities.success.setinvulnerable." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendSuccess(new TranslatableComponent("hamusuke.command.entityabilities.success.setinvulnerable." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setInvisible(CommandSourceStack source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setInvisible(flag));

		if (entities.size() == 1) {
			source.sendSuccess(new TranslatableComponent("hamusuke.command.entityabilities.success.invisible." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendSuccess(new TranslatableComponent("hamusuke.command.entityabilities.success.invisible." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}
}
