package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import com.aaronjyoder.democracy.util.Constants;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class RenameServer implements Action {

  private String name;

  public RenameServer(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return "rename-server";
  }

  @Override
  public String getEffect() {
    return "Renames the server to '" + name + "'.";
  }

  @Override
  public boolean apply(JDA jda) {
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      guild.getManager().setName(name).queue();
      return true;
    }
    return false;
  }

}
