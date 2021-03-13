package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import com.aaronjyoder.democracy.util.GovUtil;
import net.dv8tion.jda.api.JDA;

public class ChangeProposalPassThreshold implements Action {

  private int oldThreshold;
  private int newThreshold;

  public ChangeProposalPassThreshold(int threshold) {
    this.oldThreshold = GovUtil.retrieveProposalThreshold();
    this.newThreshold = threshold;
  }

  @Override
  public String getName() {
    return "change-proposal-threshold";
  }

  @Override
  public String getEffect() {
    return "Changes the percentage threshold that `yea` votes must surpass in order to pass a proposal from "
        + oldThreshold + " percent to " + newThreshold + " percent.";
  }

  @Override
  public boolean apply(JDA jda) {
    return GovUtil.saveProposalThreshold(newThreshold);
  }

}
