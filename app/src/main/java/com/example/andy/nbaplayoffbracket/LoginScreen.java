package com.example.andy.nbaplayoffbracket;

import android.os.Bundle;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;

@Layout(R.layout.login_view)
@Module(injects = LoginView.class, addsTo = LoginScreen.Module.class)
public class LoginScreen implements Blueprint {

  @Singleton
  static class Presenter extends ViewPresenter<LoginView> {

    private final Flow flow;

    @Inject
    Presenter(Flow flow) {
      this.flow = flow;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
    }

    public void loginButtonClicked(String username, String password) {
      ParseUser.logInInBackground(username, password, new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if (e != null) {
            Toast.makeText(getView().getContext(), "Wrong credentials.", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(getView().getContext(), "Successfully Logged In.", Toast.LENGTH_SHORT).show();
            flow.resetTo(new NotesListScreen());
          }
        }
      });
    }

  }

  @Override
  public String getMortarScopeName() {
    return getClass().getName();
  }

  @Override
  public Object getDaggerModule() {
    return new Module();
  }

  @dagger.Module(injects = MainActivity.class, addsTo = MainActivity.ActivityModule.class)
  class Module {
  }
}
