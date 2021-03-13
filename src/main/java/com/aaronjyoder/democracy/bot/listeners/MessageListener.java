package com.aaronjyoder.democracy.bot.listeners;

import com.aaronjyoder.democracy.Main;
import com.aaronjyoder.democracy.bot.CommandInput;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        CommandInput input = new CommandInput(event);

        String msg = input.getEvent().getMessage().getContentRaw();
        boolean isBot = input.getEvent().getAuthor().isBot();
        boolean isSelf = input.getEvent().getAuthor().getId().equals(input.getEvent().getJDA().getSelfUser().getId());
        boolean hasPrefix = msg.startsWith(Main.bot.getDefaultPrefix()) || msg.startsWith(Main.bot.getPrefix());
        boolean hasMention = input.getEvent().getMessage().isMentioned(input.getEvent().getJDA().getSelfUser(), Message.MentionType.USER) && input.getEvent().getMessage().getContentRaw().startsWith("<@!");
        boolean isPrivate = input.getEvent().isFromType(ChannelType.PRIVATE);

        boolean isForBot = (hasPrefix || hasMention || isPrivate) && !isBot && !isSelf;

        if (isForBot) {
            Main.bot.getCommandHandler().execute(input);
        }
    }

}
