package com.aaronjyoder.democracy.util;

import com.aaronjyoder.democracy.Main;
import com.aaronjyoder.democracy.government.proposal.Proposal;
import com.aaronjyoder.democracy.government.vote.Vote;
import java.util.UUID;

public class GovUtil {

  public static Proposal getProposal(UUID uuid) {
    try {
      for (Proposal p : Main.activeProposals) {
        if (p.getUUID().equals(uuid)) {
          return p;
        }
      }
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  public static Proposal getProposal(long messageID) {
    try {
      for (Proposal p : Main.activeProposals) {
        if (p.getMessageID() == messageID) {
          return p;
        }
      }
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  public static Proposal getProposal(String messageID) {
    return getProposal(Long.parseLong(messageID));
  }

  public static Vote getVote(String codePoint) {
    for (Vote v : Vote.values()) {
      if (v.getUnicode().equalsIgnoreCase(codePoint)) {
        return v;
      }
    }
    return null;
  }

  public static boolean saveProposalThreshold(int threshold) {
    GsonUtil.saveToJson(threshold, Constants.THRESH_DIR + "proposal-pass.json", Integer.class);
    return true;
  }

  public static int retrieveProposalThreshold() {
    return (Integer) GsonUtil.loadFromJson(Constants.THRESH_DIR + "proposal-pass.json", Integer.class);
  }

  public static boolean saveQuorumAmount(int amount) {
    GsonUtil.saveToJson(amount, Constants.THRESH_DIR + "quorum-amount.json", Integer.class);
    return true;
  }

  public static int retrieveQuorumAmount() {
    return (Integer) GsonUtil.loadFromJson(Constants.THRESH_DIR + "quorum-amount.json", Integer.class);
  }

  public static boolean saveVoteTime(long time) {
    GsonUtil.saveToJson(time, Constants.THRESH_DIR + "vote-time.json", Long.class);
    return true;
  }

  public static long retrieveVoteTimeMs() {
    return (Long) GsonUtil.loadFromJson(Constants.THRESH_DIR + "vote-time.json", Long.class);
  }

}
