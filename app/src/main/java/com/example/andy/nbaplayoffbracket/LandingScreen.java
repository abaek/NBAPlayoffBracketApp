package com.example.andy.nbaplayoffbracket;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;

@Layout(R.layout.landing_view)
@Module(injects = LandingView.class, addsTo = LandingScreen.Module.class)
public class LandingScreen implements Blueprint {

  @Singleton
  static class Presenter extends ViewPresenter<LandingView> {

    private final Flow flow;

    @Inject
    Presenter(Flow flow) {
      this.flow = flow;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
    }

    public void loginClicked() {
      flow.goTo(new LoginScreen());
    }

    public void signUpClicked() {
      flow.goTo(new SignUpScreen());
    }
  }

  @dagger.Module(injects = MainActivity.class, addsTo = MainActivity.ActivityModule.class)
  class Module {
  }

  @Override
  public String getMortarScopeName() {
    return "NBA Playoff Bracket";
  }

  @Override
  public Object getDaggerModule() {
    return new Module();
  }
}
