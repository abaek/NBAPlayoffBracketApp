package com.example.andy.nbaplayoffbracket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.inject.Inject;

public class LoginView extends LinearLayout {

  @Inject
  LoginScreen.Presenter presenter;

  private EditText username;
  private EditText password;
  private Button loginButton;

  public LoginView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Utils.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    Toast.makeText(getContext(), "Login View", Toast.LENGTH_SHORT).show();

    username = (EditText) findViewById(R.id.username);
    password = (EditText) findViewById(R.id.password);

    loginButton = (Button) findViewById(R.id.loginButton);
    loginButton.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        presenter.loginButtonClicked(username.getText().toString(), password.getText().toString());
      }
    });

    presenter.takeView(this);
  }
}
