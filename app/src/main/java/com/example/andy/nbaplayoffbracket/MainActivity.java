package com.example.andy.nbaplayoffbracket;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import flow.Backstack;
import flow.Flow;
import flow.Layouts;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.MortarActivityScope;
import mortar.MortarScope;

public class MainActivity extends Activity implements Flow.Listener, ActionBar.TabListener {

  private Flow flow;
  private MortarActivityScope activityScope;
  private ObjectGraph activityGraph;
  private ContainerView containerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Sets up container for flow.
    setContentView(R.layout.activity_main);
    containerView = (ContainerView) findViewById(R.id.container);

    // Mortar setup.
    MortarScope parentScope = ((BaseApplication) getApplication()).getRootScope();
    activityScope = Mortar.requireActivityScope(parentScope, new ActivityModule());
    activityScope.onCreate(savedInstanceState);

    // Dagger setup.
    activityGraph = ObjectGraph.create(new ActivityModule());
    activityGraph.inject(this);

    // Creates initial backstack.
    flow = new Flow(getInitialBackstack(savedInstanceState), this);
    go(flow.getBackstack(), Flow.Direction.FORWARD, new Flow.Callback() {
      @Override
      public void onComplete() {
        // No-op.
      }
    });

    // Sets up action bar.
    ActionBar bar = getActionBar();
    bar.setDisplayShowTitleEnabled(false);
    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    bar.addTab(bar.newTab().setText("Picks").setTabListener(this));
    bar.addTab(bar.newTab().setText("Matrix").setTabListener(this));
    bar.addTab(bar.newTab().setText("Standings").setTabListener(this));
    bar.addTab(bar.newTab().setText("Settings").setTabListener(this));
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    activityScope.onSaveInstanceState(outState);
  }

  @Override
  public Object getSystemService(String name) {
    if (Mortar.isScopeSystemService(name)) {
      return activityScope;
    }
    return super.getSystemService(name);
  }

  @Override
  public void onBackPressed() {
    if (!flow.goBack()) {
      finish();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // For up button in action bar.
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (isFinishing() && activityScope != null) {
      MortarScope parentScope = ((BaseApplication) getApplication()).getRootScope();
      parentScope.destroyChild(activityScope);
      activityScope = null;
    }
  }

  @Override
  public void go(Backstack backstack, Flow.Direction direction, Flow.Callback callback) {
    Object screen = backstack.current().getScreen();
    // No animation.
    containerView.displayView(getView(screen));
    callback.onComplete();
  }

  private Backstack getInitialBackstack(Bundle savedInstanceState) {
    // Returns PicksScreen if logged in, LandingScreen if logged out.
    if (ParseUser.getCurrentUser() != null) {
      return Backstack.single(new PicksScreen());
    }
    return Backstack.single(new LandingScreen());
  }

  private View getView(Object screen) {
    ObjectGraph graph = activityGraph.plus(screen);
    Context scopedContext = new ScopedContext(this, graph);
    return Layouts.createView(scopedContext, screen);
  }

  @Override
  public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
    // If logged in, skips this step.
    if (ParseUser.getCurrentUser() != null) {
      int pos = tab.getPosition();
      if (pos == 0) {
        flow.goTo(new PicksScreen());
      } else if (pos == 1) {
        flow.goTo(new MatrixScreen());
      } else if (pos == 2) {
        flow.goTo(new StandingsScreen());
      } else if (pos == 3) {
        flow.goTo(new SettingsScreen());
      }
    }
  }

  @Override
  public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    // No-op.
  }

  @Override
  public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    // No-op.
  }

  @Module(injects = MainActivity.class, library = true)
  class ActivityModule implements Blueprint {
    @Provides
    @Singleton
    Flow provideAppFlow() {
      return flow;
    }

    @Provides
    @Singleton
    ActionBar provideActionBar() {
      return getActionBar();
    }

    @Override
    public String getMortarScopeName() {
      return getClass().getName();
    }

    @Override
    public Object getDaggerModule() {
      return new Module();
    }

    @dagger.Module(injects = MainActivity.class)
    class Module {
    }
  }
}
