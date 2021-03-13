package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

public class Create implements Action {

  private ChannelAction<TextChannel> textChannelAction;
  private ChannelAction<VoiceChannel> voiceChannelAction;
  private ChannelAction<Category> categoryAction;
  private RoleAction roleAction;

  public Create(ChannelAction<TextChannel> textChannelAction, boolean junk) {
    this.textChannelAction = textChannelAction;
  }

  public Create(ChannelAction<VoiceChannel> voiceChannelAction, int junk) {
    this.voiceChannelAction = voiceChannelAction;
  }

  public Create(ChannelAction<Category> categoryAction) {
    this.categoryAction = categoryAction;
  }

  public Create(RoleAction roleAction) {
    this.roleAction = roleAction;
  }

  @Override
  public String getName() {
    return "create";
  }

  @Override
  public String getEffect() {
    if (textChannelAction != null) {
      return "Creates a new text channel.";
    } else if (voiceChannelAction != null) {
      return "Creates a new voice channel.";
    } else if (categoryAction != null) {
      return "Creates a new category.";
    } else if (roleAction != null) {
      return "Create a new role.";
    } else {
      return "No effect.";
    }
  }

  @Override
  public boolean apply(JDA jda) {
    if (textChannelAction != null) {
      textChannelAction.queue();
      return true;
    } else if (voiceChannelAction != null) {
      voiceChannelAction.queue();
      return true;
    } else if (categoryAction != null) {
      categoryAction.queue();
      return true;
    } else if (roleAction != null) {
      roleAction.queue();
      return true;
    } else {
      return false;
    }
  }

}
