package com.aaronjyoder.democracy.bot.commands;

import com.aaronjyoder.democracy.bot.Command;
import com.aaronjyoder.democracy.bot.CommandInput;
import com.aaronjyoder.democracy.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;

public class PermissionList extends Command {

  public PermissionList() {
    settings.setGuildOnly(true);
    settings.setAliases("permissions","permissionlist");
    settings.setDescription("Lists all permissions.");
    settings.setEmbedColor(Constants.EMBED_COLOR);
  }

  @Override
  protected void execute(CommandInput input) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setTitle("Permissions");

    StringBuilder sb = new StringBuilder();
    sb.append("```\n");
    sb.append("add_reactions").append("\n");
    sb.append("create_invite").append("\n");
    sb.append("change_nickname").append("\n");
    sb.append("\n");
    sb.append("view_channels").append("\n");
    sb.append("read_messages").append("\n");
    sb.append("send_messages").append("\n");
    sb.append("send_tts").append("\n");
    sb.append("manage_messages").append("\n");
    sb.append("embed_links").append("\n");
    sb.append("attach_files").append("\n");
    sb.append("read_history").append("\n");
    sb.append("mention_everyone").append("\n");
    sb.append("external_emoji").append("\n");
    sb.append("\n");
    sb.append("priority_speaker").append("\n");
    sb.append("voice_stream").append("\n");
    sb.append("voice_connect").append("\n");
    sb.append("voice_speak").append("\n");
    sb.append("voice_mute_others").append("\n");
    sb.append("voice_deafen_others").append("\n");
    sb.append("voice_move_others").append("\n");
    sb.append("voice_activity").append("\n");
    sb.append("\n");
    sb.append("kick_members").append("\n");
    sb.append("ban_members").append("\n");
    sb.append("manage_nicknames").append("\n");
    sb.append("manage_roles").append("\n");
    sb.append("manage_permissions").append("\n");
    sb.append("manage_channel").append("\n");
    sb.append("manage_server").append("\n");
    sb.append("manage_emoji").append("\n");
    sb.append("```");

    embedBuilder.setDescription(sb.toString());
    embedBuilder.setColor(this.settings.getEmbedColor());
    input.getEvent().getChannel().sendMessage(embedBuilder.build()).queue();
  }

}
