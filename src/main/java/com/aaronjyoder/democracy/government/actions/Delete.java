package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;

public class Delete implements Action {

  private GuildChannel channel;
  private Category category;
  private Role role;

  public Delete(GuildChannel channel) {
    this.channel = channel;
  }

  public Delete(Category category) {
    this.category = category;
  }

  public Delete(Role role) {
    this.role = role;
  }

  @Override
  public String getName() {
    return "delete";
  }

  @Override
  public String getEffect() {
    if (channel != null) {
      return "Deletes " + channel.getType().toString() + " channel #" + channel.getName() + ".";
    } else if (category != null) {
      return "Deletes category " + category.getName() + ".";
    } else if (role != null) {
      return "Deletes role " + role.getAsMention() + ".";
    } else {
      return "No effect.";
    }
  }

  @Override
  public boolean apply(JDA jda) {
    if (channel != null) {
      channel.delete().queue();
      return true;
    } else if (category != null) {
      category.delete().queue();
      return true;
    } else if (role != null) {
      role.delete().queue();
      return true;
    } else {
      return false;
    }
  }

}
