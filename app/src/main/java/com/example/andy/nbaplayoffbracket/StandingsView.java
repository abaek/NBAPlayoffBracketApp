package com.example.andy.nbaplayoffbracket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import javax.inject.Inject;

public class StandingsView extends LinearLayout {

  @Inject
  StandingsScreen.Presenter presenter;

  public ParseQueryAdapter<UserInfo> standingsListAdapter;

  private ListView standingsList;

  public StandingsView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Utils.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    // Set up the adapter.
    standingsList = (ListView) findViewById(R.id.standings_list);
    standingsListAdapter = new StandingsListAdapter(getContext());
    standingsList.setAdapter(standingsListAdapter);

    presenter.takeView(this);
  }

  private class StandingsListAdapter extends ParseQueryAdapter<UserInfo> {

    public StandingsListAdapter(Context context) {
      super(context, new ParseQueryAdapter.QueryFactory<UserInfo>() {
        public ParseQuery<UserInfo> create() {
          ParseQuery<UserInfo> query = UserInfo.getQuery();
          query.orderByDescending("score");
          return query;
        }
      });
    }

    @Override
    public View getItemView(UserInfo userInfo, View view, ViewGroup parent) {
      final ViewHolder holder;
      if (view == null) {
        view = View.inflate(getContext(), R.layout.standings_row, null);
        holder = new ViewHolder();
        holder.screenName = (TextView) view.findViewById(R.id.screen_name);
        holder.score = (TextView) view.findViewById(R.id.score);
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }

      holder.screenName.setText(userInfo.getScreenName());
      holder.score.setText(Integer.toString(userInfo.getScore()));

      return view;
    }
  }

  private static class ViewHolder {
    TextView screenName;
    TextView score;
  }
}
