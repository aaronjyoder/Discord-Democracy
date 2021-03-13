package com.aaronjyoder.democracy.government.vote;

import java.util.List;

public class VoteResult {

  private int yeas = 0;
  private int nays = 0;
  private int present = 0;
  private final int proposalThreshold;
  private final int quorum;
  String reason = null;

  public VoteResult(List<Vote> votes, int proposalThreshold, int quorum) {
    this.proposalThreshold = proposalThreshold;
    this.quorum = quorum;
    for (Vote vote : votes) {
      switch (vote) {
        case YEA -> this.yeas++;
        case NAY -> this.nays++;
        case PRESENT -> this.present++;
      }
    }
  }

  public String getReason() {
    return reason;
  }

  public int getYeas() {
    return yeas;
  }

  public int getNays() {
    return nays;
  }

  public int getPresent() {
    return present;
  }

  public int getTotalVotes(boolean includePresent) {
    return includePresent ? getYeas() + getNays() + getPresent() : getYeas() + getNays();
  }

  public float getYeaPercent(boolean includePresent) {
    return 100 * ((float) getYeas() / (float) (getTotalVotes(includePresent)));
  }

  public float getNayPercent(boolean includePresent) {
    return 100 * ((float) getNays() / (float) (getTotalVotes(includePresent)));
  }

  public float getResultPercent() {
    return getYeaPercent(false);
  }

  public boolean passes() {
    if (getTotalVotes(false) == 0) {
      this.reason = "No votes were cast.";
      return false;
    } else if (getTotalVotes(true) < quorum) {
      this.reason = "Quorum was not met.";
      return false;
    } else {
      if (getResultPercent() <= proposalThreshold) {
        this.reason = "The nays have it.";
      }
      return getResultPercent() > proposalThreshold;
    }
  }

}
