package net.frozenorb.hydrogen.util;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Date;

public class EmbedUtil {
    
    public static EmbedBuilder hEmbed(String title, String message, Color color) {
        return new EmbedBuilder()
                .setColor(color)
                .setAuthor("Hydrogen")
                .setTitle(title)
                .setDescription(message)
                .setFooter(TimeUtils.formatIntoCalendarString(new Date(System.currentTimeMillis())));
    }
}
