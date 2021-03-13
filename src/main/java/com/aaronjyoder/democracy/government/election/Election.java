package com.aaronjyoder.democracy.government.election;

import java.util.List;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class Election {

  private final Role role;
  private List<Member> candidates;

  public Election(Role role) {
    this.role = role;
  }

}
