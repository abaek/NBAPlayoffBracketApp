package com.example.andy.nbaplayoffbracket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import javax.inject.Inject;

public class SignUpView extends LinearLayout {

  @Inject
  SignUpScreen.Presenter presenter;

  private EditText username;
  private EditText screenName;
  private EditText password;
  private EditText confirmPassword;
  private Button signupButton;

  public SignUpView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Utils.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    username = (EditText) findViewById(R.id.username);
    screenName = (EditText) findViewById(R.id.screen_name);
    password = (EditText) findViewById(R.id.password);
    confirmPassword = (EditText) findViewById(R.id.confirm_password);

    signupButton = (Button) findViewById(R.id.sign_up_button);
    signupButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        presenter.registerClicked(username.getText().toString(),  screenName.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
      }
    });

    presenter.takeView(this);
  }
}
