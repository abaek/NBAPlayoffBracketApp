package com.example.andy.nbaplayoffbracket;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("UserInfo")
public class UserInfo extends ParseObject {

  public String getUserId() {
    return getString("userId");
  }

  public void setUserId(String id) {
    put("userId", id);
  }

  public String getScreenName() {
    return getString("screenName");
  }

  public void setScreenName(String name) {
    put("screenName", name);
  }

  public int getScore() {
    return getInt("score");
  }

  public void setScore(int score) {
    put("score", score);
  }

  public static ParseQuery<UserInfo> getQuery() {
    return ParseQuery.getQuery(UserInfo.class);
  }
}
