package com.aaronjyoder.democracy.bot.commands;

import com.aaronjyoder.democracy.bot.Command;
import com.aaronjyoder.democracy.bot.CommandInput;
import com.aaronjyoder.democracy.util.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class Say extends Command { // TODO: Doesn't work for some reason

  public Say() {
    settings.setOwnerCommand(true);
    settings.setAliases("say", "echo");
    settings.setDescription("Says stuff.");
    settings.setEmbedColor(Constants.EMBED_COLOR);
  }

  @Override
  protected void execute(CommandInput input) {
    Guild guild = input.getEvent().getJDA().getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      TextChannel discussion = guild.getTextChannelById(792351947554160643L);
      if (discussion != null) {
        String text = input.getArgString();
        discussion.sendMessage(text).queue();
      }
    }
  }

}
