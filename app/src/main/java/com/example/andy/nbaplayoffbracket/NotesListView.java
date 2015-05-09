package com.example.andy.nbaplayoffbracket;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import javax.inject.Inject;

public class NotesListView extends LinearLayout {

  @Inject
  NotesListScreen.Presenter presenter;

  public ParseQueryAdapter<Note> todoListAdapter;

  private EditText newNote;
  private Button addNoteButton;
  private ListView notesList;

  public NotesListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Utils.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    Toast.makeText(getContext(), "Login View", Toast.LENGTH_SHORT).show();

    newNote = (EditText) findViewById(R.id.new_note);
    addNoteButton = (Button) findViewById(R.id.add_note_button);
    addNoteButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if(newNote.length() > 0){
          presenter.addNote(newNote.getText().toString());
        }
      }
    });

    ParseQueryAdapter.QueryFactory<Note> factory = new ParseQueryAdapter.QueryFactory<Note>() {
      public ParseQuery<Note> create() {
        ParseQuery<Note> query = Note.getQuery();
        query.orderByAscending("createdAt");
        query.fromLocalDatastore();
        return query;
      }
    };

    // Set up the adapter
    todoListAdapter = new NotesListAdapter(getContext(), factory);

    notesList = (ListView) findViewById(R.id.notes_list);
    notesList.setAdapter(todoListAdapter);

    presenter.takeView(this);
  }


  private class NotesListAdapter extends ParseQueryAdapter<Note> {

    public NotesListAdapter(Context context,
                            ParseQueryAdapter.QueryFactory<Note> queryFactory) {
      super(context, queryFactory);
    }

    @Override
    public View getItemView(Note todo, View view, ViewGroup parent) {
      ViewHolder holder;
      if (view == null) {
        view = new TextView(getContext());
        holder = new ViewHolder();
        holder.todoTitle = (TextView) view;
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }
      TextView todoTitle = holder.todoTitle;
      todoTitle.setText(todo.getTitle());
      if (todo.isDraft()) {
        todoTitle.setTypeface(null, Typeface.ITALIC);
      } else {
        todoTitle.setTypeface(null, Typeface.NORMAL);
      }
      return view;
    }
  }

  private static class ViewHolder {
    TextView todoTitle;
  }


}
