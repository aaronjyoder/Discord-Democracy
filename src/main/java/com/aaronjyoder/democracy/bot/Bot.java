package com.aaronjyoder.democracy.bot;

import com.google.gson.Gson;
import com.aaronjyoder.democracy.auth.Authentication;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot {

  private final CommandHandler commandHandler = new CommandHandler();
  private final Authentication auth;

  public Bot() {
    this.auth = readAuth();
  }

  public CommandHandler getCommandHandler() {
    return commandHandler;
  }

  public Authentication getAuth() {
    return auth;
  }

  public String getDefaultPrefix() {
    return auth.getDefaultPrefix();
  }

  public String getPrefix() {
    return auth.getPrefix();
  }

  private Authentication readAuth() {
    File file = new File("res/auth/auth.json");
    if (file.exists()) {
      Gson gson = new Gson();
      try {
        Reader reader = new FileReader(file);
        return gson.fromJson(reader, Authentication.class);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public void start(@Nonnull final Object... listeners) {
    try {
//      DefaultShardManagerBuilder shardBuilder = DefaultShardManagerBuilder.createDefault(auth.getToken());
      DefaultShardManagerBuilder shardBuilder = DefaultShardManagerBuilder.createDefault(auth.getToken(), GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS,
          GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_EMOJIS,
          GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES)
          .setMemberCachePolicy(MemberCachePolicy.ALL);
      shardBuilder.addEventListeners(listeners);
      shardBuilder.build();
    } catch (LoginException e) {
      e.printStackTrace();
    }
  }

}
