package org.morsi.android.nethack.redux.trackers;

import android.app.Dialog;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mmorsi on 4/30/16.
 */
public class NoteTracker {
    HashMap<String, String> notes;

    GameTrackerActivity activity;

    public NoteTracker(GameTrackerActivity activity){
        this.activity = activity;
        notes = new HashMap<String, String>();
    }

    public void onCreate() {
        restorePrefs();
    }

    public void newTrackerPopup(){
        activity.showDialog(AndroidMenu.DIALOG_NOTES_ID);
    }

    public void reset(){
        notes = new HashMap<String, String>();
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

    SharedPreferences settings;

    private SharedPreferences sharedPrefs(){
        return activity.getSharedPreferences(PREFS_NAME, 0);
    }

    private ArrayList<String> labelsPref(){ return (ArrayList<String>)ObjectSerializer.deserialize(settings.getString("labels", ObjectSerializer.serialize(new ArrayList<String>()))); }

    private ArrayList<String> valuesPref(){ return (ArrayList<String>)ObjectSerializer.deserialize(settings.getString("values", ObjectSerializer.serialize(new ArrayList<String>()))); }

    public void restorePrefs() {
        settings = sharedPrefs();
        ArrayList<String> labels = labelsPref();
        ArrayList<String> values = valuesPref();
        for(int i = 0; i < labels.size(); ++i)
            notes.put(labels.get(i), values.get(i));
    }

    public void storeFields() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("labels", ObjectSerializer.serialize(new ArrayList<String>(notes.keySet())));
        editor.putString("values", ObjectSerializer.serialize(new ArrayList<String>(notes.values())));
        editor.commit();
    }

    public void updateOutput() {
        notesList().removeAllViews();
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

        label_tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));
        value_tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        label_tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));

        remove.setOnClickListener(new RemoveNoteListener(layout));

        layout.addView(label_tv);
        layout.addView(value_tv);
        layout.addView(remove);
    }

    class RemoveNoteListener implements Button.OnClickListener {
        LinearLayout note_to_remove;

        RemoveNoteListener(LinearLayout note) {
            note_to_remove = note;
        }

        public void onClick(View v) {
            notesList().removeView(note_to_remove);
        }
    }
}
