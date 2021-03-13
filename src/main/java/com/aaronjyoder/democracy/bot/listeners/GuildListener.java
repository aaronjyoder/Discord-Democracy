package com.aaronjyoder.democracy.bot.listeners;

import com.aaronjyoder.democracy.government.proposal.Proposal;
import com.aaronjyoder.democracy.government.vote.Vote;
import com.aaronjyoder.democracy.util.Constants;
import com.aaronjyoder.democracy.util.GovUtil;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {

  @Override
  public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
    Guild guild = event.getJDA().getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      TextChannel channel = guild.getTextChannelById(Constants.PENDING_PROPOSAL_CHANNEL_ID);
      if (channel != null) {
        if (event.getGuild().getId().equals(guild.getId()) && event.getChannel().getId().equals(channel.getId())
            && !event.getUserId().equals(event.getJDA().getSelfUser().getId())) {
          if (isVotingReaction(event.getUser(), event.retrieveMessage().complete(), event.getReactionEmote())) {
            Message message = event.retrieveMessage().complete();
            Set<String> reactedUserIDs = getUserIdSet(message);
            if (reactedUserIDs.contains(event.getUserId())) {
              for (Vote v : Vote.values()) {
                if (!event.getReactionEmote().getAsCodepoints().equalsIgnoreCase(v.getUnicode())) {
                  message.removeReaction(v.getUnicode(), event.getUser()).queue();
                }
              }
              Proposal proposal = GovUtil.getProposal(event.getMessageId());
              if (proposal != null) {
                Vote vote = GovUtil.getVote(event.getReactionEmote().getAsCodepoints());
                if (vote != null) {
                  proposal.addVote(vote);
                }
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
    Guild guild = event.getJDA().getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      TextChannel channel = guild.getTextChannelById(Constants.PENDING_PROPOSAL_CHANNEL_ID);
      if (channel != null) {
        if (event.getGuild().getId().equals(guild.getId()) && event.getChannel().getId().equals(channel.getId())
            && !event.getUserId().equals(event.getJDA().getSelfUser().getId())) {
          if (isVotingReaction(event.getUser(), event.retrieveMessage().complete(), event.getReactionEmote())) {
            Proposal proposal = GovUtil.getProposal(event.getMessageId());
            if (proposal != null) {
              Vote vote = GovUtil.getVote(event.getReactionEmote().getAsCodepoints());
              if (vote != null) {
                proposal.removeVote(vote);
              }
            }
          }
        }
      }
    }
  }

  private Set<String> getUserIdSet(Message message) {
    Set<User> reactedUsers = new HashSet<>();
    for (MessageReaction mr : message.getReactions()) {
      reactedUsers.addAll(mr.retrieveUsers().complete());
    }
    return reactedUsers.stream().map(User::getId).collect(Collectors.toSet());
  }

  private boolean isVotingReaction(User user, Message message, ReactionEmote reactionEmote) {
    if (reactionEmote.isEmote()) {
      message.removeReaction(reactionEmote.getEmote(), user).queue();
      return false;
    }
    if (Arrays.stream(Vote.values()).noneMatch(vote -> vote.getUnicode().equalsIgnoreCase(reactionEmote.getAsCodepoints()))) {
      if (user != null) {
        message.removeReaction(reactionEmote.getEmoji(), user).queue();
        return false;
      }
    }
    return true;
  }

}
