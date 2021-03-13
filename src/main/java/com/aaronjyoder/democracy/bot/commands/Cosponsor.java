package com.aaronjyoder.democracy.bot.commands;

import com.aaronjyoder.democracy.bot.Command;
import com.aaronjyoder.democracy.bot.CommandInput;
import com.aaronjyoder.democracy.government.proposal.Proposal;
import com.aaronjyoder.democracy.util.Constants;
import com.aaronjyoder.democracy.util.Error;
import com.aaronjyoder.democracy.util.GovUtil;
import java.time.Instant;
import java.util.UUID;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class Cosponsor extends Command {

  public Cosponsor() {
    settings.setGuildOnly(true);
    settings.setAliases("cosponsor", "sponsor");
    settings.setDescription("Cosponsor a proposal.");
  }

  @Override
  protected void execute(CommandInput input) {
    if (input.getArgs().length == 1) {
      try {
        Proposal toCosponsor = GovUtil.getProposal(UUID.fromString(input.getArg(0)));
        if (toCosponsor != null) {
          if (!toCosponsor.getSponsor().getId().equals(input.getEvent().getMember().getId())) {
            if (toCosponsor.addCosponsor(input.getEvent().getMember())) {
              Guild guild = input.getEvent().getJDA().getGuildById(Constants.GUILD_ID);
              if (guild != null) {
                TextChannel channel = guild.getTextChannelById(Constants.PENDING_PROPOSAL_CHANNEL_ID);
                if (channel != null) {
                  channel.editMessageById(toCosponsor.getMessageID(), toCosponsor.createPending()).queue();
                  sendSuccessMessage(input);
                } else {
                  input.getEvent().getChannel().sendMessage(Error.create("Could not find pending proposal text channel.", settings)).queue();
                }
              } else {
                input.getEvent().getChannel().sendMessage(Error.create("Could not find guild.", settings)).queue();
              }
            } else {
              input.getEvent().getChannel().sendMessage(Error.create("You are already a cosponsor.", settings)).queue();
            }
          } else {
            input.getEvent().getChannel().sendMessage(Error.create("You cannot cosponsor your own bill.", settings)).queue();
          }
        } else {
          input.getEvent().getChannel().sendMessage(Error.create("Could not find proposal.", settings)).queue();
        }
      } catch (IllegalArgumentException e) {
        input.getEvent().getChannel().sendMessage(Error.create("Invalid arguments.", settings)).queue();
      }
    } else {
      input.getEvent().getChannel().sendMessage(Error.create("Invalid arguments.", settings)).queue();
    }
  }

  private void sendSuccessMessage(CommandInput input) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Constants.SUCCESS_COLOR);
    embedBuilder.setTitle("Cosponsor Added");
    embedBuilder.setDescription("You have successfully been added as a cosponsor.");
    embedBuilder.setTimestamp(Instant.now());
    input.getEvent().getChannel().sendMessage(embedBuilder.build()).queue();
  }

}
