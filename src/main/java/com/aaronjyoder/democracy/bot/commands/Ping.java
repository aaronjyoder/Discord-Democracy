package com.aaronjyoder.democracy.bot.commands;


import com.aaronjyoder.democracy.bot.Command;
import com.aaronjyoder.democracy.bot.CommandInput;
import com.aaronjyoder.democracy.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

public class Ping extends Command {

  public Ping() {
    settings.setGuildOnly(true);
    settings.setAliases("ping");
    settings.setDescription("Tests to see if the bot is online and functional.");
    settings.setEmbedColor(Constants.EMBED_COLOR);
  }

  @Override
  protected void execute(CommandInput input) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setTitle("Ping");
    embedBuilder.setDescription("Pong! :table_tennis: | Response in `" + input.getEvent().getJDA().getRestPing().complete() + "ms`");
    embedBuilder.setColor(this.settings.getEmbedColor());
    input.getEvent().getChannel().sendMessage(embedBuilder.build()).queue();
  }

}
