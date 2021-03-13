package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import com.aaronjyoder.democracy.util.GovUtil;
import net.dv8tion.jda.api.JDA;

public class ChangeVoteTime implements Action {

  private long oldTime;
  private long newTime;

  public ChangeVoteTime(long time) {
    this.oldTime = GovUtil.retrieveVoteTimeMs();
    this.newTime = time;
  }

  @Override
  public String getName() {
    return "change-vote-time";
  }

  @Override
  public String getEffect() {
    return "Changes the amount of time given for voting on proposals from " + oldTime + "ms to " + newTime + "ms.";
  }

  @Override
  public boolean apply(JDA jda) {
    return GovUtil.saveVoteTime(newTime);
  }

}
