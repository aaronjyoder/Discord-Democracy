package com.aaronjyoder.democracy.bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandInput {

    private final MessageReceivedEvent event;
    private String commandAlias;
    private String[] arguments;

    public CommandInput(MessageReceivedEvent event) {
        this.event = event;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public void setCommandAlias(String commandAlias) {
        this.commandAlias = commandAlias;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public String getCommandAlias() {
        return commandAlias;
    }

    public String[] getArgs() {
        return arguments;
    }

    public String getArg(int i) {
        return arguments[i];
    }

    public String getArgString() {
        StringBuilder sb = new StringBuilder();
        for (String s : arguments) {
            sb.append(s).append(" ");
        }
        return sb.toString().trim();
    }

}
