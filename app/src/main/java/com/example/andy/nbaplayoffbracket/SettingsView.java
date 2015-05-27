package com.example.andy.nbaplayoffbracket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import javax.inject.Inject;

public class SettingsView extends LinearLayout {

  @Inject
  SettingsScreen.Presenter presenter;

  private Button logoutButton;

  public SettingsView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Utils.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    logoutButton = (Button) findViewById(R.id.logout_button);
    logoutButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.logout();
      }
    });

    presenter.takeView(this);
  }
}
