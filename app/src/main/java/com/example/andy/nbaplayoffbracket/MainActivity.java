package com.example.andy.nbaplayoffbracket;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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

    setContentView(R.layout.activity_main);
    containerView = (ContainerView) findViewById(R.id.container);

    MortarScope parentScope = ((BaseApplication) getApplication()).getRootScope();
    activityScope = Mortar.requireActivityScope(parentScope, new ActivityModule());
    activityScope.onCreate(savedInstanceState);

    activityGraph = ObjectGraph.create(new ActivityModule());
    activityGraph.inject(this);

    flow = new Flow(getInitialBackstack(savedInstanceState), this);
    go(flow.getBackstack(), Flow.Direction.FORWARD, new Flow.Callback() {
      @Override
      public void onComplete() {
        // No-op.
      }
    });

    ActionBar bar = getActionBar();
    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    bar.setDisplayShowTitleEnabled(false);
    bar.addTab(bar.newTab().setText("Home").setTabListener(this));
    bar.addTab(bar.newTab().setText("Picks").setTabListener(this));
    bar.addTab(bar.newTab().setText("Standings").setTabListener(this));
    bar.addTab(bar.newTab().setText("Matrix").setTabListener(this));
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
    containerView.displayView(getView(screen), direction);
    callback.onComplete();
  }

  private Backstack getInitialBackstack(Bundle savedInstanceState) {
    return Backstack.single(new LandingScreen());
  }

  private View getView(Object screen) {
    ObjectGraph graph = activityGraph.plus(screen);
    Context scopedContext = new ScopedContext(this, graph);
    return Layouts.createView(scopedContext, screen);
  }

  @Override
  public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

  }

  @Override
  public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

  }

  @Override
  public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

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
