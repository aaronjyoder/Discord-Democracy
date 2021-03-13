package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import com.aaronjyoder.democracy.util.Constants;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class Kick implements Action {

  private Member member;

  public Kick(Member member) {
    this.member = member;
  }

  @Override
  public String getName() {
    return "kick";
  }

  @Override
  public String getEffect() {
    return "Kicks " + member.getEffectiveName() + " from the server.";
  }

  @Override
  public boolean apply(JDA jda) {
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      guild.kick(member).queue();
      return true;
    }
    return false;
  }

}
