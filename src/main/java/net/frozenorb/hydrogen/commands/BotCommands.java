package net.frozenorb.hydrogen.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.frozenorb.hydrogen.Hydrogen;
import net.frozenorb.hydrogen.connection.RequestHandler;
import net.frozenorb.hydrogen.util.EmbedUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class BotCommands extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("api")) {
            event.getInteraction().replyEmbeds(EmbedUtil.hEmbed("API Status:", Hydrogen.status(), RequestHandler.isApiDown() ? Color.RED : Color.GREEN).build()).queue();
        }
    }
}
