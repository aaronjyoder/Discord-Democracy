package com.aaronjyoder.democracy.government.vote;

public enum Vote {

  YEA(":ballot_box_with_check:", "U+2611"),
  NAY(":x:", "U+274C"),
  PRESENT(":o:", "U+2B55");

  private String emoji;
  private String unicode;

  Vote(String emoji, String unicode) {
    this.emoji = emoji;
    this.unicode = unicode;
  }

  public String getEmoji() {
    return emoji;
  }

  public String getUnicode() {
    return unicode;
  }

}
