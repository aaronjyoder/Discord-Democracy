package com.aaronjyoder.democracy.bot.commands;

import com.aaronjyoder.democracy.bot.Command;
import com.aaronjyoder.democracy.bot.CommandInput;
import com.aaronjyoder.democracy.government.actions.Ban;
import com.aaronjyoder.democracy.government.actions.ChangeProposalPassThreshold;
import com.aaronjyoder.democracy.government.actions.ChangeQuorumAmount;
import com.aaronjyoder.democracy.government.actions.ChangeVoteTime;
import com.aaronjyoder.democracy.government.actions.Create;
import com.aaronjyoder.democracy.government.actions.Delete;
import com.aaronjyoder.democracy.government.actions.Grant;
import com.aaronjyoder.democracy.government.actions.Kick;
import com.aaronjyoder.democracy.government.actions.PinMessage;
import com.aaronjyoder.democracy.government.actions.RenameServer;
import com.aaronjyoder.democracy.government.actions.Resolution;
import com.aaronjyoder.democracy.government.actions.Revoke;
import com.aaronjyoder.democracy.government.proposal.Action;
import com.aaronjyoder.democracy.government.proposal.Proposal;
import com.aaronjyoder.democracy.util.Constants;
import com.aaronjyoder.democracy.util.Error;
import com.aaronjyoder.democracy.util.GovUtil;
import java.time.Instant;
import java.util.Arrays;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class Propose extends Command {

  public Propose() {
    settings.setGuildOnly(true);
    settings.setAliases("propose", "proposal");
    settings.setDescription("Propose a change.");
    // d!propose [name] | [reason] | [ACTION]
  }

  @Override
  protected void execute(CommandInput input) {
    Guild guild = input.getEvent().getJDA().getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      guild.loadMembers().onSuccess(success -> {
        if (guild.getMembers().size() >= GovUtil.retrieveQuorumAmount()) {
          if (input.getArgString().split("\\|").length > 2) {
            String[] args = input.getArgString().split("\\|");
            String name = args[0].trim();
            String reason = args[1].trim();
            Action action = parseAction(input.getEvent().getJDA(), input, args[2]);
            if (action != null) {
              Proposal proposal = new Proposal(input.getEvent().getMember(), name, reason, action);
              if (proposal.introduce(input.getEvent().getJDA())) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Constants.SUCCESS_COLOR);
                embedBuilder.setTitle("Proposal Introduced");
                embedBuilder.setDescription("Your proposal has been successfully introduced.");
                embedBuilder.setFooter("The proposal may now be voted on.");
                embedBuilder.setTimestamp(Instant.now());
                input.getEvent().getChannel().sendMessage(embedBuilder.build()).queue();
              } else {
                input.getEvent().getChannel().sendMessage(Error.create("A critical error has occurred and the command could not be completed.", settings)).queue();
              }
            } else {
              input.getEvent().getChannel().sendMessage(Error.create("Invalid action.", settings)).queue();
            }
          } else {
            input.getEvent().getChannel().sendMessage(Error.create("Invalid arguments.", settings)).queue();
          }
        } else {
          input.getEvent().getChannel().sendMessage(
              Error
                  .create("There must be " + GovUtil.retrieveQuorumAmount() + " members in order to form a quorum.\nI currently see " + guild.getMembers().size() + " members.",
                      settings))
              .queue();
        }
      });
    } else {
      input.getEvent().getChannel().sendMessage(Error.create("Could not retrieve guild.", settings)).queue();
    }
  }

  private Action parseAction(JDA jda, CommandInput input, String string) {
    String[] split = string.trim().split("\s");
    String actionStr = split[0].toLowerCase();
    String[] args = Arrays.copyOfRange(split, 1, split.length);
    return switch (actionStr) {
      default -> null;
      case "ban" -> parseBan(jda, input, args);
      case "kick" -> parseKick(jda, input, args);
      case "create" -> parseCreate(jda, input, args);
      case "delete" -> parseDelete(jda, input, args);
      case "grant" -> parseGrant(jda, input, args);
      case "revoke" -> parseRevoke(jda, input, args);
      case "pin" -> parsePinMessage(jda, input, args);
      case "rename-server" -> parseRenameServer(jda, input, args);
      case "resolution" -> parseResolution(jda, input, args);
      case "change-proposal-threshold" -> parseProposalThreshold(jda, input, args);
      case "change-quorum-amount" -> parseQuorumAmount(jda, input, args);
      case "change-vote-time" -> parseVoteTime(jda, input, args);
    };
  }

  private Action parseBan(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      if (input.getEvent().getMessage().getMentionedMembers().size() == 1) {
        Member member = input.getEvent().getMessage().getMentionedMembers().get(0);
        if (member != null) {
          result = new Ban(member);
        }
      }
    }
    return result;
  }

  private Action parseKick(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      if (input.getEvent().getMessage().getMentionedMembers().size() == 1) {
        Member member = input.getEvent().getMessage().getMentionedMembers().get(0);
        if (member != null) {
          result = new Kick(member);
        }
      }
    }
    return result;
  }

  private Action parseCreate(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      if (args.length >= 2) {
        String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        switch (args[0].trim().toLowerCase()) {
          case "textchannel" -> {
            if (name.startsWith("-")) {
              name = name.replaceFirst("-", "").toLowerCase();
            }
            name = name.replaceAll("[ \\[\\{\\}\\]\\~\\`\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\=\\+\\/\\?\\<\\>\\,\\.\\\\]", "");
            name = name.replaceAll("-", ""); // TODO: Temporary solution
            result = new Create(guild.createTextChannel(name.trim()), false);
          }
          case "voicechannel" -> result = new Create(guild.createVoiceChannel(name.trim()), 0);
          case "category" -> result = new Create(guild.createCategory(name.trim()));
          case "role" -> result = new Create(guild.createRole().setMentionable(true).setName(name.trim()));
        }
      }
    }
    return result;
  }

  private Action parseDelete(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      if (args.length == 2) {
        switch (args[0].trim().toLowerCase()) {
          case "channel" -> {
            if (input.getEvent().getMessage().getMentionedChannels().size() == 1) {
              result = new Delete(input.getEvent().getMessage().getMentionedChannels().get(0));
            } else if (args[1].trim().matches("\\d{18}")) {
              result = new Delete(guild.getGuildChannelById(args[1].trim()));
            }
          }
          case "role" -> {
            if (input.getEvent().getMessage().getMentionedRoles().size() == 1) {
              result = new Delete(input.getEvent().getMessage().getMentionedRoles().get(0));
            } else if (args[1].trim().matches("\\d{18}")) {
              result = new Delete(guild.getRoleById(args[1].trim()));
            }
          }
          case "category" -> result = new Delete(guild.getCategoryById(args[1].trim()));
        }
      }
    }
    return result;
  }

  private Action parseGrant(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      Role role = null;
      Member member = null;
      Permission permission = null;
      if (args[0].trim().equalsIgnoreCase("everyone")) {
        role = guild.getPublicRole();
      } else if (input.getEvent().getMessage().getMentionedRoles().size() == 1) {
        role = input.getEvent().getMessage().getMentionedRoles().get(0);
      } else if (args[0].trim().matches("\\d{18}")) {
        role = guild.getRoleById(args[0].trim());
      }

      if (input.getEvent().getMessage().getMentionedMembers().size() == 1) {
        member = input.getEvent().getMessage().getMentionedMembers().get(0);
      } else if (args[1].trim().matches("\\d{18}")) {
        member = guild.getMemberById(args[1].trim());
      } else {
        permission = parsePermission(args[1].toLowerCase().trim());
      }

      if (role != null && member != null && !guild.getPublicRole().equals(role)) {
        result = new Grant(member, role);
      } else if (role != null && permission != null) {
        result = new Grant(role, permission);
      }
    }
    return result;
  }

  private Action parseRevoke(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    Guild guild = jda.getGuildById(Constants.GUILD_ID);
    if (guild != null) {
      Role role = null;
      Member member = null;
      Permission permission = null;
      if (args[0].trim().equalsIgnoreCase("everyone")) {
        role = guild.getPublicRole();
      } else if (input.getEvent().getMessage().getMentionedRoles().size() == 1) {
        role = input.getEvent().getMessage().getMentionedRoles().get(0);
      } else if (args[0].trim().matches("\\d{18}")) {
        role = guild.getRoleById(args[0].trim());
      }

      if (input.getEvent().getMessage().getMentionedMembers().size() == 1) {
        member = input.getEvent().getMessage().getMentionedMembers().get(0);
      } else if (args[1].trim().matches("\\d{18}")) {
        member = guild.getMemberById(args[1].trim());
      } else {
        permission = parsePermission(args[1].toLowerCase().trim());
      }

      if (role != null && member != null && !guild.getPublicRole().equals(role)) {
        result = new Revoke(member, role);
      } else if (role != null && permission != null) {
        result = new Revoke(role, permission);
      }
    }
    return result;
  }

  private Action parsePinMessage(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    if (args.length == 2) {
      Guild guild = jda.getGuildById(Constants.GUILD_ID);
      TextChannel textChannel;
      Message message = null;
      if (input.getEvent().getMessage().getMentionedChannels().size() == 1 && args[1].trim().matches("\\d{18}")) {
        if (guild != null) {
          textChannel = input.getEvent().getMessage().getMentionedChannels().get(0);
          if (textChannel != null) {
            message = textChannel.retrieveMessageById(args[1].trim()).complete();
          }
        }
      } else if (args[0].matches("\\d{18}") && args[1].matches("\\d{18}")) {
        if (guild != null) {
          textChannel = guild.getTextChannelById(args[0].trim());
          if (textChannel != null) {
            message = textChannel.retrieveMessageById(args[1].trim()).complete();
          }
        }
      }
      if (message != null) {
        result = new PinMessage(message);
      }
    }
    return result;
  }

  private Action parseRenameServer(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    if (args.length > 0) {
      String name = String.join(" ", args);
      result = new RenameServer(name);
    }
    return result;
  }

  private Action parseResolution(JDA jda, CommandInput input, String[] args) {
    return new Resolution();
  }

  // Change Files

  private Action parseProposalThreshold(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    if (args.length == 1) {
      try {
        int threshold = Integer.parseInt(args[0]);
        if (threshold >= 1 && threshold <= 100) { // minimum 1%, maximum 100%
          result = new ChangeProposalPassThreshold(threshold);
        }
      } catch (NumberFormatException e) {
        return null;
      }
    }
    return result;
  }

  private Action parseQuorumAmount(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    if (args.length == 1) {
      try {
        int amount = Integer.parseInt(args[0]);
        if (amount > 2) { // minimum of 3 members
          result = new ChangeQuorumAmount(amount);
        }
      } catch (NumberFormatException e) {
        return null;
      }
    }
    return result;
  }

  private Action parseVoteTime(JDA jda, CommandInput input, String[] args) {
    Action result = null;
    if (args.length == 1) {
      try {
        long time = Long.parseLong(args[0]);
        if (time >= 5 * 60 * 1000) { // minimum of 5 minutes
          result = new ChangeVoteTime(time);
        }
      } catch (NumberFormatException e) {
        return null;
      }
    }
    return result;
  }

  // Other

  private Permission parsePermission(String permission) {
    return switch (permission.toLowerCase()) {
      case "create_invite" -> Permission.CREATE_INSTANT_INVITE;
      case "kick_members" -> Permission.KICK_MEMBERS;
      case "ban_members" -> Permission.BAN_MEMBERS;
      case "manage_channel" -> Permission.MANAGE_CHANNEL;
      case "manage_server" -> Permission.MANAGE_SERVER;
      case "add_reactions" -> Permission.MESSAGE_ADD_REACTION;
      case "priority_speaker" -> Permission.PRIORITY_SPEAKER;
      case "view_channels" -> Permission.VIEW_CHANNEL;
      case "read_messages" -> Permission.MESSAGE_READ;
      case "send_messages" -> Permission.MESSAGE_WRITE;
      case "send_tts" -> Permission.MESSAGE_TTS;
      case "manage_messages" -> Permission.MESSAGE_MANAGE;
      case "embed_links" -> Permission.MESSAGE_EMBED_LINKS;
      case "attach_files" -> Permission.MESSAGE_ATTACH_FILES;
      case "read_history" -> Permission.MESSAGE_HISTORY;
      case "mention_everyone" -> Permission.MESSAGE_MENTION_EVERYONE;
      case "external_emoji" -> Permission.MESSAGE_EXT_EMOJI;
      case "voice_stream" -> Permission.VOICE_STREAM;
      case "voice_connect" -> Permission.VOICE_CONNECT;
      case "voice_speak" -> Permission.VOICE_SPEAK;
      case "voice_mute_others" -> Permission.VOICE_MUTE_OTHERS;
      case "voice_deafen_others" -> Permission.VOICE_DEAF_OTHERS;
      case "voice_move_others" -> Permission.VOICE_MOVE_OTHERS;
      case "voice_activity" -> Permission.VOICE_USE_VAD;
      case "change_nickname" -> Permission.NICKNAME_CHANGE;
      case "manage_nicknames" -> Permission.NICKNAME_MANAGE;
      case "manage_roles" -> Permission.MANAGE_ROLES;
      case "manage_permissions" -> Permission.MANAGE_PERMISSIONS;
      case "manage_emoji" -> Permission.MANAGE_EMOTES;
      default -> Permission.UNKNOWN;
    };
  }

}
