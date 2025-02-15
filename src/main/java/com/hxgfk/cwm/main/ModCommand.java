package com.hxgfk.cwm.main;

import com.hxgfk.cwm.chat.IMobChat;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.server.command.EnumArgument;

public class ModCommand {
    enum Options {
        REMOVE_ALL_CHAT_HISTORY,
        RESET_ALL_STATE,
        SET_TEMPERATURE,
        RANDOM_TEMPERATURE,
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("cwm").requires((css) -> css.hasPermission(2))
                .then(Commands.argument("option", EnumArgument.enumArgument(Options.class))
                        .then(Commands.argument("entity", EntityArgument.entities())
                        .executes(ModCommand::run)
                        .then(Commands.argument("value", FloatArgumentType.floatArg(0, 2.0f))
                        .executes(ModCommand::run)))
                )
        );
    }

    public static int run(CommandContext<CommandSourceStack> source) throws CommandSyntaxException {
        switch (source.getArgument("option", Options.class)) {
            case REMOVE_ALL_CHAT_HISTORY:
                for (Entity entity : EntityArgument.getEntities(source, "entity")) {
                    if (entity instanceof IMobChat chat) {
                        chat.clearMsgList();
                    }
                }
                source.getSource().sendSuccess(Component.translatable("msg.command.success.removeAllHistory").withStyle(ChatFormatting.GREEN), true);
                break;
            case RESET_ALL_STATE:
                for (Entity entity : EntityArgument.getEntities(source, "entity")) {
                    if (entity instanceof IMobChat chat) {
                        chat.setChatting(false);
                    }
                }
                source.getSource().sendSuccess(Component.translatable("msg.command.success.resetState").withStyle(ChatFormatting.GREEN), true);
                break;
            case SET_TEMPERATURE:
                float t = source.getArgument("value", Float.class);
                for (Entity entity : EntityArgument.getEntities(source, "entity")) {
                    if (entity instanceof IMobChat chat) {
                        chat.setTemperature(t);
                    }
                }
                source.getSource().sendSuccess(Component.translatable("msg.command.success.setTemp").withStyle(ChatFormatting.GREEN), true);
                break;
            case RANDOM_TEMPERATURE:
                for (Entity entity : EntityArgument.getEntities(source, "entity")) {
                    if (entity instanceof IMobChat chat) {
                        chat.setTemperature(chat.randomTemperature());
                    }
                }
                source.getSource().sendSuccess(Component.translatable("msg.command.success.randTemp").withStyle(ChatFormatting.GREEN), true);
                break;
            default:
                source.getSource().sendFailure(Component.translatable("msg.command.err.invalidOption", source.getArgument("option", Options.class)).withStyle(ChatFormatting.RED));
        }
        return 0;
    }
}
