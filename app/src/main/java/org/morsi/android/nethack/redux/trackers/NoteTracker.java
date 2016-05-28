package org.morsi.android.nethack.redux.trackers;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.morsi.android.nethack.redux.GameTrackerActivity;
import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.morsi.android.nethack.redux.util.ObjectSerializer;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteTracker {
    HashMap<String, String> notes;

    GameTrackerActivity activity;

    public NoteTracker(GameTrackerActivity activity){
        this.activity = activity;
        notes = new HashMap<String, String>();
    }

    public void onCreate() {
        restorePrefs();
        updateOutput();
    }

    public void newTrackerPopup(){
        activity.showDialog(AndroidMenu.DIALOG_NOTES_ID);
    }

    public void reset(){
        notes.clear();
        storeFields();
        removeAllViews();
    }

    public void addNote(String label, String note){
        notes.put(label, note);
    }

    ///

    private LinearLayout notesList(){
        return (LinearLayout) activity.findViewById(R.id.notes_list);
    }

    ///

    // store values persistently
    public static final String PREFS_NAME = "NotesTrackerValues";

    private SharedPreferences sharedPrefs(){
        return activity.getSharedPreferences(PREFS_NAME, 0);
    }

    private ArrayList<String> labelsPref(){
        return (ArrayList<String>)ObjectSerializer.deserialize(sharedPrefs().getString("labels", ObjectSerializer.serialize(new ArrayList<String>())));
    }

    private ArrayList<String> valuesPref(){
        return (ArrayList<String>)ObjectSerializer.deserialize(sharedPrefs().getString("values", ObjectSerializer.serialize(new ArrayList<String>())));
    }

    public void restorePrefs() {
        ArrayList<String> labels = labelsPref();
        ArrayList<String> values = valuesPref();
        for(int i = 0; i < labels.size(); ++i)
            notes.put(labels.get(i), values.get(i));
    }

    public void storeFields() {
        SharedPreferences.Editor editor = sharedPrefs().edit();
        editor.putString("labels", ObjectSerializer.serialize(new ArrayList<String>(notes.keySet())));
        editor.putString("values", ObjectSerializer.serialize(new ArrayList<String>(notes.values())));
        editor.commit();
    }

    private void removeAllViews(){
        notesList().removeAllViews();
    }

    public void updateOutput() {
        removeAllViews();
        for(String label : notes.keySet())
            displayNote(label, notes.get(label));
    }

    private void displayNote(String label, String value){
        LinearLayout layout = new LinearLayout(activity);
        TextView label_tv   = new TextView(activity);
        TextView value_tv   = new TextView(activity);
        ImageView remove    = new ImageView(activity);

        label_tv.setText(label);
        value_tv.setText(value);
        remove.setBackgroundResource(R.drawable.minus);

        label_tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));
        value_tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f));
        remove.setLayoutParams(new LinearLayout.LayoutParams(0, 30, 0.05f));

        label_tv.setPadding(10, 0, 0, 0);
        remove.setPadding(0, 0, 10, 0);

        remove.setOnClickListener(new RemoveNoteListener(label, layout));

        layout.addView(label_tv);
        layout.addView(value_tv);
        layout.addView(remove);

        notesList().addView(layout);
    }

    class RemoveNoteListener implements Button.OnClickListener {
        String note_to_remove;
        LinearLayout note_view_to_remove;

        RemoveNoteListener(String note, LinearLayout view) {
            note_to_remove = note;
            note_view_to_remove = view;
        }

        public void onClick(View v) {
            notes.remove(note_to_remove);
            notesList().removeView(note_view_to_remove);
            storeFields();
        }
    }
}
