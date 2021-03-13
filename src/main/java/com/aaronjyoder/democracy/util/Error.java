package com.aaronjyoder.democracy.util;

import com.aaronjyoder.democracy.bot.CommandSettings;
import java.time.Instant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Error {

  public static MessageEmbed create(String desc, CommandSettings settings) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Constants.ERROR_COLOR);
    embedBuilder.setTitle("Error: " + settings.getName());
    embedBuilder.setDescription(desc);
//    embedBuilder.setFooter("");
    embedBuilder.setTimestamp(Instant.now());
    return embedBuilder.build();
  }

  public static MessageEmbed create(String desc, String errorStr) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Constants.ERROR_COLOR);
    embedBuilder.setTitle("Error: " + errorStr);
    embedBuilder.setDescription(desc);
//    embedBuilder.setFooter("");
    embedBuilder.setTimestamp(Instant.now());
    return embedBuilder.build();
  }

}
