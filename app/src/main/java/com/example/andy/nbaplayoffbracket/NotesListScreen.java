package com.example.andy.nbaplayoffbracket;

import android.os.Bundle;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;

@Layout(R.layout.notes_list_view)
@Module(injects = NotesListView.class, addsTo = NotesListScreen.Module.class)
public class NotesListScreen implements Blueprint {

  @Singleton
  static class Presenter extends ViewPresenter<NotesListView> {

    private final Flow flow;

    @Inject
    Presenter(Flow flow) {
      this.flow = flow;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
    }

    public void addNote(String title) {
      Note newNote = new Note();
      newNote.setUuidString();
      newNote.setTitle(title);
      newNote.setAuthor(ParseUser.getCurrentUser());
      newNote.pinInBackground(new SaveCallback() {
//      newNote.saveInBackground(new SaveCallback() {
        @Override
        public void done(ParseException e) {
          getView().todoListAdapter.loadObjects();
        }
      });
    }
  }

  @dagger.Module(injects = MainActivity.class, addsTo = MainActivity.ActivityModule.class)
  class Module {
  }

  @Override
  public String getMortarScopeName() {
    return getClass().getName();
  }

  @Override
  public Object getDaggerModule() {
    return new Module();
  }
}
