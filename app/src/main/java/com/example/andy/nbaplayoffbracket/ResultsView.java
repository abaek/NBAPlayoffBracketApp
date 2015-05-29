package com.example.andy.nbaplayoffbracket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static java.util.Arrays.asList;

public class ResultsView extends LinearLayout {

  @Inject
  ResultsScreen.Presenter presenter;

  public ParseQueryAdapter<Game> resultsListAdapter;

  private ListView resultsList;
  private HashMap<String, String> userScreenNames = new HashMap<>();
  private HashMap<String, List<String>> winGamesToUserIds = new HashMap<>();
  private HashMap<String, List<String>> loseGamesToUserIds = new HashMap<>();

  public ResultsView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Utils.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    // Set up the adapter
    resultsList = (ListView) findViewById(R.id.results_list);
    resultsListAdapter = new ResultsListAdapter(getContext());

    ParseQuery<ParseObject> screenNamesQuery = ParseQuery.getQuery("UserInfo");
    screenNamesQuery.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> usersList, ParseException e) {
        for (ParseObject user : usersList) {
          userScreenNames.put(user.getString("userId"), user.getString("screenName"));
        }

        // Query ALL picks and order them into win and lose hashmaps by game id and user ids.
        ParseQuery<ParseObject> picksQuery = ParseQuery.getQuery("Pick");
        picksQuery.findInBackground(new FindCallback<ParseObject>() {
          public void done(List<ParseObject> picksList, ParseException e) {
            for (ParseObject pick : picksList) {
              // Voted for win.
              if (pick.getBoolean("result")) {
                // Add new user id to game id.
                if (winGamesToUserIds.containsKey(pick.getString("gameId"))) {
                  ArrayList<String> userIds = (ArrayList) winGamesToUserIds.get(pick.getString("gameId"));
                  userIds.add(userScreenNames.get(pick.getString("userId")));
                  winGamesToUserIds.put(pick.getString("gameId"), userIds);
                }
                // Set new array list.
                else {
                  winGamesToUserIds.put(pick.getString("gameId"), new ArrayList<>(asList(userScreenNames.get(pick.getString("userId")))));
                }
              }
              // Voted for loss.
              else {
                // Add new user id to game id.
                if (loseGamesToUserIds.containsKey(pick.getString("gameId"))) {
                  ArrayList<String> userIds = (ArrayList) loseGamesToUserIds.get(pick.getString("gameId"));
                  userIds.add(userScreenNames.get(pick.getString("userId")));
                  loseGamesToUserIds.put(pick.getString("gameId"), userIds);
                }
                // Set new array list.
                else {
                  loseGamesToUserIds.put(pick.getString("gameId"), new ArrayList<>(asList(userScreenNames.get(pick.getString("userId")))));
                }
              }
            }
            // Set adapter after picks and users have been loaded.
            resultsList.setAdapter(resultsListAdapter);
          }
        });
      }
    });

    presenter.takeView(this);
  }

  private class ResultsListAdapter extends ParseQueryAdapter<Game> {

    public ResultsListAdapter(Context context) {
      super(context, new ParseQueryAdapter.QueryFactory<Game>() {
        public ParseQuery<Game> create() {
          ParseQuery<Game> query = Game.getQuery();
          query.orderByAscending("gameDate");
          return query;
        }
      });
    }

    @Override
    public View getItemView(Game game, View view, ViewGroup parent) {
      final ViewHolder holder;
      if (view == null) {
        view = View.inflate(getContext(), R.layout.result_row, null);
        holder = new ViewHolder();
        holder.gameId = game.getObjectId();
        holder.homeUsers = (ListView) view.findViewById(R.id.home_users);
        holder.awayUsers = (ListView) view.findViewById(R.id.away_users);
        holder.homeTeamName = (TextView) view.findViewById(R.id.home_team_name);
        holder.awayTeamName = (TextView) view.findViewById(R.id.away_team_name);
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }

      holder.homeTeamName.setText(game.getHomeTeam());
      holder.awayTeamName.setText(game.getAwayTeam());

      List<String> homeTeamUsers = new ArrayList<>();
      if (winGamesToUserIds.containsKey(holder.gameId)) {
        homeTeamUsers = winGamesToUserIds.get(holder.gameId);
      }
      ArrayAdapter<String> homeAdapter = new ArrayAdapter<>(getContext(), R.layout.user_list_left, homeTeamUsers);
      holder.homeUsers.setAdapter(homeAdapter);

      List<String> awayTeamUsers = new ArrayList<>();
      if (loseGamesToUserIds.containsKey(holder.gameId)) {
        awayTeamUsers = loseGamesToUserIds.get(holder.gameId);
      }
      ArrayAdapter<String> awayAdapter = new ArrayAdapter<>(getContext(), R.layout.user_list_right, awayTeamUsers);
      holder.awayUsers.setAdapter(awayAdapter);

      return view;
    }
  }

  private static class ViewHolder {
    String gameId;
    TextView homeTeamName;
    TextView awayTeamName;
    ListView homeUsers;
    ListView awayUsers;
  }
}
