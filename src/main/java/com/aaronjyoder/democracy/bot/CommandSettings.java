package com.aaronjyoder.democracy.bot;

import java.awt.Color;
import net.dv8tion.jda.api.Permission;

public class CommandSettings {

  private boolean isOwnerCommand;
  private boolean isGuildOnly;
  private Permission[] authorPermissions;
  private Permission[] selfPermissions;
  private String[] aliases;
  private String description;
  private Color embedColor = new Color(128, 128, 128);
  private String category;

  // Getters

  public boolean isOwnerCommand() {
    return isOwnerCommand;
  }

  public boolean isGuildOnly() {
    return isGuildOnly;
  }

  public Permission[] getAuthorPermissions() {
    return authorPermissions;
  }

  public Permission[] getSelfPermissions() {
    return selfPermissions;
  }

  public String[] getAliases() {
    return aliases;
  }

  public String getName() {
    return aliases[0];
  }

  public String getDescription() {
    return description;
  }

  public Color getEmbedColor() {
    return embedColor;
  }

  public String getCategory() {
    return category;
  }

  // Setters

  public void setOwnerCommand(boolean isOwnerCommand) {
    this.isOwnerCommand = isOwnerCommand;
  }

  public void setGuildOnly(boolean isGuildOnly) {
    this.isGuildOnly = isGuildOnly;
  }

  public void setAuthorPerms(Permission... authorPermissions) {
    this.authorPermissions = authorPermissions;
  }

  public void setSelfPerms(Permission... selfPermissions) {
    this.selfPermissions = selfPermissions;
  }

  public void setAliases(String... aliases) {
    this.aliases = aliases;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setEmbedColor(int r, int g, int b) {
    this.embedColor = new Color(r, g, b);
  }

  public void setEmbedColor(Color color) {
    this.embedColor = color;
  }

  public void setCategory(String category) {
    this.category = category;
  }

}
