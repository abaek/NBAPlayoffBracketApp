package com.example.andy.nbaplayoffbracket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.inject.Inject;

public class LandingView extends LinearLayout {

  @Inject
  LandingScreen.Presenter presenter;

  private Button loginButton;
  private Button signUpButton;

  public LandingView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Utils.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    Toast.makeText(getContext(), "Landing View", Toast.LENGTH_SHORT).show();

    loginButton = (Button) findViewById(R.id.login);
    loginButton.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        presenter.loginClicked();
      }
    });

    signUpButton = (Button) findViewById(R.id.sign_up);
    signUpButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.signUpClicked();
      }
    });

    presenter.takeView(this);
  }
}
