package com.example.andy.nbaplayoffbracket;

import android.app.ActionBar;
import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;

@Layout(R.layout.standings_view)
@Module(injects = StandingsView.class, addsTo = StandingsScreen.Module.class)
public class StandingsScreen implements Blueprint {

  @Singleton
  static class Presenter extends ViewPresenter<StandingsView> {

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
