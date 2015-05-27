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
  private EditText password;
  private Button signupButton;

  public SignUpView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Utils.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    username = (EditText) findViewById(R.id.username);
    password = (EditText) findViewById(R.id.password);

    signupButton = (Button) findViewById(R.id.sign_up_button);
    signupButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        presenter.registerClicked(username.getText().toString(), password.getText().toString());
      }
    });

    presenter.takeView(this);
  }
}
