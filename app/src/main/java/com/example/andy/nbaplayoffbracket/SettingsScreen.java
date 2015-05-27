package com.example.andy.nbaplayoffbracket;

import android.app.ActionBar;
import android.os.Bundle;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;

@Layout(R.layout.settings_view)
@Module(injects = SettingsView.class, addsTo = SettingsScreen.Module.class)
public class SettingsScreen implements Blueprint {

  @Singleton
  static class Presenter extends ViewPresenter<SettingsView> {

    private final Flow flow;
    private final ActionBar actionBar;

    @Inject
    Presenter(Flow flow, ActionBar actionBar) {
      this.flow = flow;
      this.actionBar = actionBar;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
      actionBar.show();
    }

    public void logout() {
      ParseUser.logOutInBackground(new LogOutCallback() {
        @Override
        public void done(ParseException e) {
          flow.replaceTo(new LandingScreen());
        }
      });
    }
  }

  @dagger.Module(injects = MainActivity.class, addsTo = MainActivity.ActivityModule.class)
  class Module {
  }

  @Override
  public String getMortarScopeName() {
    return getClass().getName();
  }

  @Override
  public Object getDaggerModule() {
    return new Module();
  }
}
