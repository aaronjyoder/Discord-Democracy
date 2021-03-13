package com.aaronjyoder.democracy.bot;

import com.aaronjyoder.democracy.Main;
import net.dv8tion.jda.api.entities.ChannelType;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.net.URL;
import java.util.*;

public class CommandHandler {

    public static final List<Command> commands = new ArrayList<>();

    public CommandHandler() {
        try {
            Set<URL> classPathList = new HashSet<>(ClasspathHelper.forJavaClassPath());
            Set<Class<? extends Command>> result = new Reflections(
                    new ConfigurationBuilder().setScanners(new SubTypesScanner()).setUrls(classPathList))
                    .getSubTypesOf(Command.class);

            for (Class<? extends Command> c : result) {
                String[] categoryString = c.getPackage().toString().split("\\.");
                String category = categoryString[categoryString.length - 1];
                if (category.equalsIgnoreCase("commands")) {
                    category = "default";
                }
                Command command = c.getDeclaredConstructor().newInstance();
                command.settings.setCategory(category);
                commands.add(command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the input, then executes the command if it exists.
     */
    public void execute(CommandInput input) {
        Command result = parse(input);
        if (result != null && isValid(input, result)) {
            result.execute(input);
        }
    }

    /**
     * Figures out what the command is.
     */
    private Command parse(CommandInput input) {
        Command result = null;
        String msg = input.getEvent().getMessage().getContentRaw();

        boolean startsWithDefaultPrefix = msg.startsWith(Main.bot.getDefaultPrefix());
        boolean startsWithPrefix = msg.startsWith(Main.bot.getPrefix());
        boolean startsWithMention = msg.startsWith("<@!" + input.getEvent().getJDA().getSelfUser().getId() + ">");
        boolean isPrivateChannel = input.getEvent().isFromType(ChannelType.PRIVATE);

        String[] args = null;

        if (startsWithDefaultPrefix) {
            args = msg.replaceFirst(Main.bot.getDefaultPrefix(), "").trim().split("\\s+");
        } else if (startsWithPrefix) {
            args = msg.replaceFirst(Main.bot.getPrefix(), "").trim().split("\\s+");
        } else if (startsWithMention) {
            args = msg.replaceFirst("<@!" + input.getEvent().getJDA().getSelfUser().getId() + ">", "").trim().split("\\s+");
        } else if (isPrivateChannel) {
            args = msg.trim().split("\\s+");
        }

        if (args == null) {
            return null;
        }

        input.setCommandAlias(args[0]); // Command alias should always be the first entry
        input.setArguments(args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length)); // Skip command alias, if there are no args set it as empty array

        for (Command command : commands) {
            if (Arrays.asList(command.settings.getAliases()).contains(input.getCommandAlias())) {
                result = command;
                break;
            }
        }

        return result;
    }

    /**
     * Determines if the command is valid to use based on who is using it and the settings for that command.
     */
    private boolean isValid(CommandInput input, Command c) {
        if (c.settings.isOwnerCommand() && !input.getEvent().getAuthor().getId().equals(Main.bot.getAuth().getOwnerID())) {
            return false;
        }
        if (c.settings.isGuildOnly() && !input.getEvent().isFromType(ChannelType.TEXT)) {
            return false;
        }
        if (c.settings.getSelfPermissions() != null && !input.getEvent().getGuild().getSelfMember().hasPermission(c.settings.getSelfPermissions())) {
            return false;
        }
        if (c.settings.getAuthorPermissions() != null && !input.getEvent().getMember().hasPermission(c.settings.getAuthorPermissions())) { // technically could have left guild by the time this happens
            return false;
        }
        return true;
    }

}
