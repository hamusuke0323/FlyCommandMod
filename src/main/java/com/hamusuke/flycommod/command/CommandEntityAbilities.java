package com.hamusuke.flycommod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.stream.Collectors;

public class CommandEntityAbilities {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> literalArgumentBuilder = Commands.literal("entityabilities").requires((permission) -> permission.hasPermission(2));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("noGravity").then(Commands.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return noGravity(command.getSource(), EntityArgument.getEntities(command, "targets").stream().filter(entity -> entity.isNoGravity() != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("setGlowing").then(Commands.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setGlowing(command.getSource(), EntityArgument.getEntities(command, "targets").stream().filter(entity -> entity.isGlowing() != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("setInvulnerable").then(Commands.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setInvulnerable(command.getSource(), EntityArgument.getEntities(command, "targets").stream().filter(entity -> entity.isInvulnerable() != flag).collect(Collectors.toList()), flag);
		}))));

		literalArgumentBuilder.then(Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("setInvisible").then(Commands.argument("boolean", BoolArgumentType.bool()).executes((command) -> {
			boolean flag = BoolArgumentType.getBool(command, "boolean");
			return setInvisible(command.getSource(), EntityArgument.getEntities(command, "targets").stream().filter(entity -> entity.isInvisible() != flag).collect(Collectors.toList()), flag);
		}))));

		dispatcher.register(literalArgumentBuilder);
	}

	private static int noGravity(CommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> {
			entity.setNoGravity(flag);
			if (!flag) {
				entity.fallDistance = -(float) (entity.getY() + 10.0D);
			}
		});

		if (entities.size() == 1) {
			source.sendSuccess(new TranslationTextComponent("hamusuke.command.entityabilities.success.nogravity." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendSuccess(new TranslationTextComponent("hamusuke.command.entityabilities.success.nogravity." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setGlowing(CommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setGlowing(flag));

		if (entities.size() == 1) {
			source.sendSuccess(new TranslationTextComponent("hamusuke.command.entityabilities.success.setglowing." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendSuccess(new TranslationTextComponent("hamusuke.command.entityabilities.success.setglowing." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setInvulnerable(CommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setInvulnerable(flag));

		if (entities.size() == 1) {
			source.sendSuccess(new TranslationTextComponent("hamusuke.command.entityabilities.success.setinvulnerable." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendSuccess(new TranslationTextComponent("hamusuke.command.entityabilities.success.setinvulnerable." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}

	private static int setInvisible(CommandSource source, Collection<? extends Entity> entities, boolean flag) {
		entities.forEach((entity) -> entity.setInvisible(flag));

		if (entities.size() == 1) {
			source.sendSuccess(new TranslationTextComponent("hamusuke.command.entityabilities.success.invisible." + flag + ".single", entities.iterator().next().getDisplayName()), true);
		} else {
			source.sendSuccess(new TranslationTextComponent("hamusuke.command.entityabilities.success.invisible." + flag + ".multiple", entities.size()), true);
		}

		return entities.size();
	}
}