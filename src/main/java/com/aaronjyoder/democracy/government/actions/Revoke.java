package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import com.aaronjyoder.democracy.util.Constants;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class Revoke implements Action {

  private Member member;
  private Role role;
  private Permission permission;

  public Revoke(Member member, Role role) {
    this.member = member;
    this.role = role;
  }

  public Revoke(Role role, Permission permission) {
    this.role = role;
    this.permission = permission;
  }

  @Override
  public String getName() {
    return "revoke";
  }

  @Override
  public String getEffect() {
    if (role != null && member != null) {
      return "Revokes role " + role.getAsMention() + " from member " + member.getAsMention() + ".";
    } else if (role != null && permission != null) {
      return "Revokes permission " + permission.getName() + " from role " + role.getAsMention() + ".";
    } else {
      return "No effect.";
    }
  }

  @Override
  public boolean apply(JDA jda) {
    if (role != null && member != null) {
      Guild guild = jda.getGuildById(Constants.GUILD_ID);
      if (guild != null) {
        guild.removeRoleFromMember(member, role).queue();
        return true;
      }
      return false;
    } else if (role != null && permission != null) {
      role.getManager().revokePermissions(permission).queue();
      return true;
    } else {
      return false;
    }
  }

}
