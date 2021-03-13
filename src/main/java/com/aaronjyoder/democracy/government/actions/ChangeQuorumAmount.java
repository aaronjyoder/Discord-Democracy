package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import com.aaronjyoder.democracy.util.GovUtil;
import net.dv8tion.jda.api.JDA;

public class ChangeQuorumAmount implements Action {

  private int oldAmount;
  private int newAmount;

  public ChangeQuorumAmount(int amount) {
    this.oldAmount = GovUtil.retrieveQuorumAmount();
    this.newAmount = amount;
  }

  @Override
  public String getName() {
    return "change-quorum-amount";
  }

  @Override
  public String getEffect() {
    return "Changes the quorum amount from " + oldAmount + " members to " + newAmount + " members.";
  }

  @Override
  public boolean apply(JDA jda) {
    return GovUtil.saveQuorumAmount(newAmount);
  }

}
