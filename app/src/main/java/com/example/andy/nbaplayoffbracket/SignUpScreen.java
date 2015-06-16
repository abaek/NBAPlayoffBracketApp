package com.example.andy.nbaplayoffbracket;

import android.os.Bundle;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
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

    public void registerClicked(final String username, final String screenName, final String password, String confirmPassword) {
      if (!password.equals(confirmPassword)) {
        Toast.makeText(getView().getContext(), "Passwords don't match.", Toast.LENGTH_SHORT).show();
      } else {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              // Automatically login after signing up.
              ParseUser.logInInBackground(username, password, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                  if (user != null) {

                    // Save user screen name.
                    ParseObject userInfo = ParseObject.create("UserInfo");
                    userInfo.put("userId", user.getObjectId());
                    userInfo.put("screenName", screenName);
                    userInfo.put("score", 0);
                    userInfo.saveInBackground();

                    flow.resetTo(new PicksScreen());
                  }
                }
              });
            }
          }
        });
      }
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
