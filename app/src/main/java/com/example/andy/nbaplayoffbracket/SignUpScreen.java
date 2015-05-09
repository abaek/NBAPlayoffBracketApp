package com.example.andy.nbaplayoffbracket;

import android.os.Bundle;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;

@Layout(R.layout.sign_up_view)
@Module(injects = SignUpView.class, addsTo = SignUpScreen.Module.class)
public class SignUpScreen implements Blueprint {

  @Singleton
  static class Presenter extends ViewPresenter<SignUpView> {

    private final Flow flow;

    @Inject
    Presenter(Flow flow) {
      this.flow = flow;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
    }

    public void registerClicked(String username, String password) {
      ParseUser user = new ParseUser();
      user.setUsername(username);
      user.setPassword(password);
      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if (e == null) {
            flow.replaceTo(new LoginScreen());
          }
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
