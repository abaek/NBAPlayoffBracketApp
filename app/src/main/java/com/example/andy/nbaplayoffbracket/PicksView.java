package com.example.andy.nbaplayoffbracket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

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

    private HashMap<String, Boolean> picks = new HashMap<>();

    public GamesListAdapter(Context context) {
      super(context, new ParseQueryAdapter.QueryFactory<Game>() {
        public ParseQuery<Game> create() {
          ParseQuery<Game> query = Game.getQuery();
          query.orderByAscending("gameDate");
          return query;
        }
      });

      ParseQuery<ParseObject> query = ParseQuery.getQuery("Pick");
      query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
      query.findInBackground(new FindCallback<ParseObject>() {
        public void done(List<ParseObject> picksList, ParseException e) {
          if (e == null) {
            for (ParseObject pick : picksList) {
              picks.put(pick.getString("gameId"), pick.getBoolean("result"));
            }
          }
        }
      });
    }

    @Override
    public View getItemView(Game game, View view, ViewGroup parent) {
      final ViewHolder holder;
      if (view == null) {
        view = View.inflate(getContext(), R.layout.game_row, null);
        holder = new ViewHolder();
        holder.homeTeamName = (TextView) view.findViewById(R.id.home_team_name);
        holder.awayTeamName = (TextView) view.findViewById(R.id.away_team_name);
        holder.homeTeamBackground = (FrameLayout) view.findViewById(R.id.home_team_background);
        holder.awayTeamBackground = (FrameLayout) view.findViewById(R.id.away_team_background);
        holder.gameId = game.getObjectId();
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }
      holder.homeTeamName.setText(game.getHomeTeam());
      holder.awayTeamName.setText(game.getAwayTeam());

      // Set appropriate background color.
      if (picks.containsKey(game.getObjectId())) {
        // Picked home team to win.
        if (picks.get(game.getObjectId())) {
          holder.homeTeamBackground.setBackgroundColor(getResources().getColor(R.color.win_color));
          holder.awayTeamBackground.setBackgroundColor(getResources().getColor(R.color.lose_color));
        } else {
          holder.awayTeamBackground.setBackgroundColor(getResources().getColor(R.color.win_color));
          holder.homeTeamBackground.setBackgroundColor(getResources().getColor(R.color.lose_color));
        }
      } else {
        holder.homeTeamBackground.setBackgroundColor(getResources().getColor(R.color.lose_color));
        holder.awayTeamBackground.setBackgroundColor(getResources().getColor(R.color.lose_color));
      }

      holder.homeTeamBackground.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (picks.containsKey(holder.gameId)) {

          } else {
            ParseObject gameScore = new ParseObject("Pick");
            gameScore.put("result", true);
            gameScore.put("userId", ParseUser.getCurrentUser().getObjectId());
            gameScore.put("gameId", holder.gameId);
            gameScore.saveInBackground();
            picks.put(holder.gameId, true);
          }
          holder.awayTeamBackground.setBackgroundColor(getResources().getColor(R.color.lose_color));
          holder.homeTeamBackground.setBackgroundColor(getResources().getColor(R.color.win_color));
        }
      });

      holder.awayTeamBackground.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (picks.containsKey(holder.gameId)) {

          } else {
            ParseObject gameScore = new ParseObject("Pick");
            gameScore.put("result", false);
            gameScore.put("userId", ParseUser.getCurrentUser().getObjectId());
            gameScore.put("gameId", holder.gameId);
            gameScore.saveInBackground();
            picks.put(holder.gameId, false);
          }
          holder.homeTeamBackground.setBackgroundColor(getResources().getColor(R.color.lose_color));
          holder.awayTeamBackground.setBackgroundColor(getResources().getColor(R.color.win_color));
        }
      });
      return view;
    }
  }

  private static class ViewHolder {
    String gameId;
    TextView homeTeamName;
    TextView awayTeamName;
    TextView gameNumber;
    FrameLayout homeTeamBackground;
    FrameLayout awayTeamBackground;
  }
}
