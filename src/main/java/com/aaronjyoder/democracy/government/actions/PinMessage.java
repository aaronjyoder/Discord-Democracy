package com.aaronjyoder.democracy.government.actions;

import com.aaronjyoder.democracy.government.proposal.Action;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;

public class PinMessage implements Action {

  private Message message;

  public PinMessage(Message message) {
    this.message = message;
  }

  @Override
  public String getName() {
    return "pin";
  }

  @Override
  public String getEffect() {
    return "Pins this message: " + message.getJumpUrl();
  }

  @Override
  public boolean apply(JDA jda) {
    message.pin().queue();
    return true;
  }

}
