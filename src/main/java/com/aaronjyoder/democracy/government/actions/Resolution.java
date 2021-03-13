package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import net.dv8tion.jda.api.JDA;

public class Resolution implements Action {

  public Resolution() {
  }

  @Override
  public String getName() {
    return "resolution";
  }

  @Override
  public String getEffect() {
    return "Sets into effect the text of this resolution.";
  }

  @Override
  public boolean apply(JDA jda) {
    return true;
  }

}
