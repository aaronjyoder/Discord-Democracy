package com.aaronjyoder.democracy.government.proposal;

import net.dv8tion.jda.api.JDA;

public interface Action {

  String getName();

  String getEffect();

  /**
   * This will do the action.
   */
  boolean apply(JDA jda);

}
