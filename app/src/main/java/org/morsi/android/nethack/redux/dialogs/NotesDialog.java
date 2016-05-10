package org.morsi.android.nethack.redux.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.morsi.android.nethack.redux.GameTrackerActivity;
import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.trackers.NoteTracker;
import org.morsi.android.nethack.redux.trackers.PlayerTracker;

public class NotesDialog extends Dialog{
    Activity activity;

    GameTrackerActivity game_tracker(){
        return (GameTrackerActivity) activity;
    }

    NoteTracker note_tracker(){
        return game_tracker().note_tracker;
    }

    private Button closeButton(){
        return (Button) findViewById(R.id.notes_close);
    }

    private EditText labelInput(){ return (EditText) findViewById(R.id.notesLabelInput); }

    private String labelValue(){ return labelInput().getText().toString(); }

    private EditText valueInput(){ return (EditText) findViewById(R.id.notesValueInput); }

    private String valueValue(){ return valueInput().getText().toString(); }

    private void resetDialog(){
        labelInput().setText("");
        valueInput().setText("");
        labelInput().requestFocus();
    }

    ///

    public NotesDialog(Activity activity){
        super(activity);
        this.activity = activity;

        // create new dialog
        setContentView(R.layout.notes_dialog);
        setTitle("Notes");

        // wire up close button
        dialog_listener = new NotesDialogListener(this);
        closeButton().setOnClickListener(dialog_listener);

        // expand dialog
        // XXX fill_parent param in layout isn't being applied coorectly
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.FILL_PARENT;
        getWindow().setAttributes(params);
    }

    NotesDialogListener dialog_listener;

    class NotesDialogListener extends DialogListener{
        NotesDialogListener(Dialog dialog){
            super(dialog);
        }

        public void onClick (View v){
            if(!labelValue().equals("") && !valueValue().equals("")) {
                note_tracker().addNote(labelValue(), valueValue());
                note_tracker().storeFields();
                note_tracker().updateOutput();

            }

            resetDialog();

            super.onClick(v);
        }
    }
}
