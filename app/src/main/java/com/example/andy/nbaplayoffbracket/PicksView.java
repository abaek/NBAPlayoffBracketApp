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

public class PicksView extends LinearLayout {

  @Inject
  PicksScreen.Presenter presenter;

  public ParseQueryAdapter<Game> gamesListAdapter;

  private ListView gamesList;

  public PicksView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Utils.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    // Set up the adapter
    gamesList = (ListView) findViewById(R.id.games_list);
    gamesListAdapter = new GamesListAdapter(getContext());
    gamesList.setAdapter(gamesListAdapter);

    presenter.takeView(this);
  }

  private class GamesListAdapter extends ParseQueryAdapter<Game> {

    public GamesListAdapter(Context context) {
      super(context, new ParseQueryAdapter.QueryFactory<Game>() {
        public ParseQuery<Game> create() {
          ParseQuery<Game> query = Game.getQuery();
          query.orderByAscending("gameDate");
          return query;
        }
      });
    }

    @Override
    public View getItemView(Game todo, View view, ViewGroup parent) {
      ViewHolder holder;
      if (view == null) {
        view = View.inflate(getContext(), R.layout.game_row, null);
        holder = new ViewHolder();
        holder.homeTeamName = (TextView) view.findViewById(R.id.home_team_name);
        holder.awayTeamName = (TextView) view.findViewById(R.id.away_team_name);
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }
      TextView homeTeamName = holder.homeTeamName;
      homeTeamName.setText(todo.getHomeTeam());
      TextView awayTeamName = holder.awayTeamName;
      awayTeamName.setText(todo.getAwayTeam());
      return view;
    }
  }

  private static class ViewHolder {
    TextView homeTeamName;
    TextView awayTeamName;
    TextView gameNumber;
  }
}
