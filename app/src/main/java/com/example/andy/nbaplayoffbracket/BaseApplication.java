package com.example.andy.nbaplayoffbracket;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import mortar.Mortar;
import mortar.MortarScope;

public class BaseApplication extends Application {
  private MortarScope rootScope;

  @Override
  public void onCreate() {
    super.onCreate();
    rootScope = Mortar.createRootScope(BuildConfig.DEBUG);

    ParseObject.registerSubclass(Game.class);
    ParseObject.registerSubclass(UserInfo.class);
    Parse.initialize(this, "XMlHOICLdsocoC7Zy0x3sf9i6PFmIQmfyZ96xKw4", "wBEZtH5EpDKxLmSqFe3Ibr2VqbhA28rnYg45Q8iP");  }

  public MortarScope getRootScope() {
    return rootScope;
  }
}

