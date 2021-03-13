package com.aaronjyoder.democracy;

import com.aaronjyoder.democracy.bot.Bot;
import com.aaronjyoder.democracy.bot.listeners.GuildListener;
import com.aaronjyoder.democracy.bot.listeners.MessageListener;
import com.aaronjyoder.democracy.bot.listeners.ReadyListener;
import com.aaronjyoder.democracy.government.proposal.Proposal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class Main {

  public static final Instant START_TIME = Instant.now();
  public static final Bot bot = new Bot();

  public static Set<Proposal> activeProposals = new HashSet<>();

  public static void main(String[] args) {
    try {
      bot.start(new ReadyListener(), new MessageListener(), new GuildListener());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
