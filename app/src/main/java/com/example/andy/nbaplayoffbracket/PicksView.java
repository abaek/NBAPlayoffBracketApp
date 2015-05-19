package com.example.andy.nbaplayoffbracket;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import javax.inject.Inject;

public class PicksView extends LinearLayout {

  @Inject
  PicksScreen.Presenter presenter;

  public PicksView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Utils.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    presenter.takeView(this);
  }
}
