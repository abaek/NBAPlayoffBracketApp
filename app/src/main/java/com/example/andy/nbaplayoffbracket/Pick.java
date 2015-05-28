package com.example.andy.nbaplayoffbracket;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.UUID;

@ParseClassName("Pick")
public class Pick extends ParseObject {

  public String getGameId() {
    return getString("gameId");
  }

  public void setGameId(String gameId) {
    put("gameId", gameId);
  }

  public String getUserId() {
    return getString("userId");
  }

  public void setUserId(String userId) {
    put("userId", userId);
  }

  public boolean getResult() {
    return getBoolean("result");
  }

  public void setResult(boolean result) {
    put("result", result);
  }

  public String getUuidString() {
    return getString("uuid");
  }

  public void setUuidString() {
    UUID uuid = UUID.randomUUID();
    put("uuid", uuid.toString());
  }

  public static ParseQuery<Pick> getQuery() {
    return ParseQuery.getQuery(Pick.class);
  }
}
