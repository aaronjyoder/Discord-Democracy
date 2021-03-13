package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import com.aaronjyoder.democracy.util.Constants;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class Ban implements Action {

  private Member member;

  public Ban(Member member) {
    this.member = member;
  }


  @Override
  public String getName() {
    return "ban";
  }

  @Override
  public String getEffect() {
    return "Bans " + member.getEffectiveName() + " from the server.";
  }

  @Override
  public boolean apply(JDA jda) {
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      guild.ban(member.getUser(), 0).queue();
      return true;
    }
    return false;
  }

}
