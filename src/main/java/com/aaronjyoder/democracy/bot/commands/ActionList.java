package com.aaronjyoder.democracy.bot.commands;

import com.aaronjyoder.democracy.bot.Command;
import com.aaronjyoder.democracy.bot.CommandInput;
import com.aaronjyoder.democracy.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

public class ActionList extends Command {

  public ActionList() {
    settings.setGuildOnly(true);
    settings.setAliases("actions","actionlist");
    settings.setDescription("Lists all actions.");
    settings.setEmbedColor(Constants.EMBED_COLOR);
  }

  @Override
  protected void execute(CommandInput input) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setTitle("Actions");

    StringBuilder sb = new StringBuilder();
    sb.append("```\n");
    sb.append("resolution").append("\n");
    sb.append("\n");
    sb.append("pin [#channel] [messageID]").append("\n");
    sb.append("rename-server [name]").append("\n");
    sb.append("\n");
    sb.append("kick [@member]").append("\n");
    sb.append("ban [@member]").append("\n");
    sb.append("create textchannel [name]").append("\n");
    sb.append("create voicechannel [name]").append("\n");
    sb.append("create category [name]").append("\n");
    sb.append("create role [name]").append("\n");
    sb.append("delete [#channel]").append("\n");
    sb.append("delete [@role]").append("\n");
    sb.append("delete [categoryID]").append("\n");
    sb.append("\n");
    sb.append("grant [@role] [@member]").append("\n");
    sb.append("grant [@role] [permission]").append("\n");
    sb.append("\n");
    sb.append("revoke [@role] [@member]").append("\n");
    sb.append("revoke [@role] [permission]").append("\n");
    sb.append("\n");
    sb.append("change-proposal-threshold [percent]").append("\n");
    sb.append("change-quorum-amount [amount]").append("\n");
    sb.append("change-vote-time [time in milliseconds]").append("\n");
    sb.append("```");

    embedBuilder.setDescription(sb.toString());
    embedBuilder.setColor(this.settings.getEmbedColor());
    input.getEvent().getChannel().sendMessage(embedBuilder.build()).queue();
  }

}
