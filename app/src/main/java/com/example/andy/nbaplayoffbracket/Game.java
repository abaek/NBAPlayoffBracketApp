package com.example.andy.nbaplayoffbracket;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.UUID;

@ParseClassName("Game")
public class Game extends ParseObject {

  public String getHomeTeam() {
    return getString("home");
  }

  public void setHomeTeam(String home) {
    put("home", home);
  }

  public String getAwayTeam() {
    return getString("away");
  }

  public void setAwayTeam(String away) {
    put("away", away);
  }

  public Date getGameDate() {
    return getDate("gameDate");
  }

  public void setGameDate(Date gameDate) {
    put("gameDate", gameDate);
  }

  public int getGameNumber() {
    return getInt("gameNumber");
  }

  public void setGameNumer(int gameNumber) {
    put("gameNumber", gameNumber);
  }

  public void setUuidString() {
    UUID uuid = UUID.randomUUID();
    put("uuid", uuid.toString());
  }

  public String getUuidString() {
    return getString("uuid");
  }

  public static ParseQuery<Game> getQuery() {
    return ParseQuery.getQuery(Game.class);
  }
}
