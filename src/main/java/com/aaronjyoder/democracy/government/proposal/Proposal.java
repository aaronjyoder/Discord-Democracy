package com.aaronjyoder.democracy.government.proposal;

import com.aaronjyoder.democracy.Main;
import com.aaronjyoder.democracy.government.vote.Vote;
import com.aaronjyoder.democracy.government.vote.VoteResult;
import com.aaronjyoder.democracy.util.Constants;
import com.aaronjyoder.democracy.util.GovUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class Proposal {

  // TODO: Consider if the sponsor/cosponsors are null at any point
  private UUID uuid;
  private long messageID;
  private final Member sponsor;
  private final Set<Member> cosponsors;
  private final String name;
  private final String reason;
  private final Action action;
  private List<Vote> votes = new ArrayList<>();
  private Timer timer;
  private int proposalThreshold;
  private int quorum;

  public Proposal(Member sponsor, String name, String reason, Action action) {
    this.uuid = UUID.randomUUID();
    this.sponsor = sponsor;
    this.cosponsors = new HashSet<>();
    this.name = name;
    this.reason = reason;
    this.action = action;
    this.timer = new Timer();
    this.proposalThreshold = GovUtil.retrieveProposalThreshold();
    this.quorum = GovUtil.retrieveQuorumAmount();
  }

  public UUID getUUID() {
    return uuid;
  }

  public long getMessageID() {
    return messageID;
  }

  public Member getSponsor() {
    return sponsor;
  }

  public boolean addCosponsor(Member cosponsor) {
    return cosponsors.add(cosponsor);
  }

  public boolean removeCosponsor(Member cosponsor) {
    return cosponsors.remove(cosponsor);
  }

  public void addVote(Vote vote) {
    this.votes.add(vote);
  }

  public void removeVote(Vote vote) {
    this.votes.remove(vote);
  }

  public boolean introduce(JDA jda) {
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      TextChannel pendingProposalChannel = guild.getTextChannelById(Constants.PENDING_PROPOSAL_CHANNEL_ID);
      if (pendingProposalChannel != null) {
        pendingProposalChannel.sendMessage(createPending()).queue(success -> {
          success.addReaction(Vote.YEA.getUnicode()).queue(yea -> success.addReaction(Vote.NAY.getUnicode()).queue(nay -> success.addReaction(Vote.PRESENT.getUnicode()).queue()));
          this.messageID = success.getIdLong();
          Main.activeProposals.add(this);
          this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
              tallyVotes(jda);
            }
          }, GovUtil.retrieveVoteTimeMs());
        });
        return true;
      }
      return false;
    }
    return false;
  }

  public void enact(JDA jda, VoteResult vr) {
    try {
      if (action.apply(jda)) {
        Guild guild = jda.getGuildById(Constants.GUILD_ID);
        if (guild != null) {
          TextChannel pending = guild.getTextChannelById(Constants.PENDING_PROPOSAL_CHANNEL_ID);
          TextChannel enacted = guild.getTextChannelById(Constants.ENACTED_PROPOSAL_CHANNEL_ID);
          if (pending != null && enacted != null) {
            pending.deleteMessageById(this.getMessageID()).queue();
            enacted.sendMessage(createEnacted(vr)).queue();
          }
        }
        Main.activeProposals.remove(this);
      }
    } catch (Exception e) {
      squash(jda, vr, "Exception could not be handled.");
    }
  }

  public void squash(JDA jda, VoteResult vr, String reason) {
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      TextChannel pending = guild.getTextChannelById(Constants.PENDING_PROPOSAL_CHANNEL_ID);
      TextChannel squashed = guild.getTextChannelById(Constants.SQUASHED_PROPOSAL_CHANNEL_ID);
      if (pending != null && squashed != null) {
        pending.deleteMessageById(this.getMessageID()).queue();
        squashed.sendMessage(createSquashed(vr, reason)).queue();
      }
    }
    Main.activeProposals.remove(this);
  }

  private void tallyVotes(JDA jda) {
    VoteResult result = new VoteResult(votes, proposalThreshold, quorum);
    if (result.passes()) {
      enact(jda, result);
    } else {
      squash(jda, result, result.getReason());
    }
  }

  public MessageEmbed createPending() {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Constants.EMBED_COLOR);
    embedBuilder.setTitle(name);
    embedBuilder.setAuthor(sponsor.getEffectiveName(), null, sponsor.getUser().getEffectiveAvatarUrl());
    embedBuilder.setDescription(this.reason);
    embedBuilder.addField("Action", "Name: **" + action.getName() + "**\nEffect: *" + action.getEffect() + "*", false);
    if (cosponsors.size() > 0) {
      embedBuilder.addField("Cosponsors", String.join(", ", cosponsors.stream().map(Member::getEffectiveName).collect(Collectors.toSet())), false);
    }

    StringBuilder rBuilder = new StringBuilder();
    rBuilder.append("**Vote**: ").append(">").append(proposalThreshold).append("% required to pass").append("\n");
    rBuilder.append("**Quorum**: ").append(quorum).append("\n");

    embedBuilder.addField("Requirements", rBuilder.toString(), false);
    embedBuilder.setFooter("ID: " + uuid.toString());
    embedBuilder.setTimestamp(Instant.now());
    return embedBuilder.build();
  }

  public MessageEmbed createEnacted(VoteResult vr) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Constants.SUCCESS_COLOR);
    embedBuilder.setTitle(name);
    embedBuilder.setAuthor(sponsor.getEffectiveName(), null, sponsor.getUser().getEffectiveAvatarUrl());
    embedBuilder.setDescription(this.reason);
    embedBuilder.addField("Action", "Name: **" + action.getName() + "**\nEffect: *" + action.getEffect() + "*", false);
    if (cosponsors.size() > 0) {
      embedBuilder.addField("Cosponsors", String.join(", ", cosponsors.stream().map(Member::getEffectiveName).collect(Collectors.toSet())), false);
    }

    StringBuilder rBuilder = new StringBuilder();
    rBuilder.append("**Vote**: ").append(">").append(proposalThreshold).append("% required to pass").append("\n");
    rBuilder.append("**Quorum**: ").append(quorum).append("\n");

    StringBuilder vrBuilder = new StringBuilder();
    vrBuilder.append("**Yea**: ").append(vr.getYeas()).append(" (").append(String.format("%.1f", vr.getYeaPercent(false))).append("%)").append("\n");
    vrBuilder.append("**Nay**: ").append(vr.getNays()).append(" (").append(String.format("%.1f", vr.getNayPercent(false))).append("%)").append("\n");
    vrBuilder.append("**Present**: ").append(vr.getPresent()).append("\n");

    embedBuilder.addField("Requirements", rBuilder.toString(), false);
    embedBuilder.addField("Tabulation", vrBuilder.toString(), false);
    embedBuilder.setFooter("This proposal has been enacted.");
    embedBuilder.setTimestamp(Instant.now());
    return embedBuilder.build();
  }

  public MessageEmbed createSquashed(VoteResult vr, String reason) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Constants.ERROR_COLOR);
    embedBuilder.setTitle(name);
    embedBuilder.setAuthor(sponsor.getEffectiveName(), null, sponsor.getUser().getEffectiveAvatarUrl());
    embedBuilder.setDescription(this.reason);
    embedBuilder.addField("Action", "Name: **" + action.getName() + "**\nEffect: *" + action.getEffect() + "*", false);
    if (cosponsors.size() > 0) {
      embedBuilder.addField("Cosponsors", String.join(", ", cosponsors.stream().map(Member::getEffectiveName).collect(Collectors.toSet())), false);
    }

    StringBuilder rBuilder = new StringBuilder();
    rBuilder.append("**Vote**: ").append(">").append(proposalThreshold).append("% required to pass").append("\n");
    rBuilder.append("**Quorum**: ").append(quorum).append("\n");

    StringBuilder vrBuilder = new StringBuilder();
    vrBuilder.append("**Yea**: ").append(vr.getYeas()).append(" (").append(String.format("%.1f", vr.getYeaPercent(false))).append("%)").append("\n");
    vrBuilder.append("**Nay**: ").append(vr.getNays()).append(" (").append(String.format("%.1f", vr.getNayPercent(false))).append("%)").append("\n");
    vrBuilder.append("**Present**: ").append(vr.getPresent()).append("\n");

    embedBuilder.addField("Requirements", rBuilder.toString(), false);
    embedBuilder.addField("Tabulation", vrBuilder.toString(), false);
    if (reason != null && !reason.isEmpty()) {
      embedBuilder.addField("Failure Reason", reason, false);
    }
    embedBuilder.setFooter("This proposal has been squashed.");
    embedBuilder.setTimestamp(Instant.now());
    return embedBuilder.build();
  }

}
