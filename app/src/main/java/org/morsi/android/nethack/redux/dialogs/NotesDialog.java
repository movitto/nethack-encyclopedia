package org.morsi.android.nethack.redux.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.morsi.android.nethack.redux.GameTrackerActivity;
import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.trackers.NoteTracker;
import org.morsi.android.nethack.redux.trackers.PlayerTracker;

public class NotesDialog {
    Activity activity;

    Dialog dialog;

    GameTrackerActivity game_tracker(){
        return (GameTrackerActivity) activity;
    }

    NoteTracker note_tracker(){
        return game_tracker().note_tracker;
    }

    private Button closeButton(){
        return (Button) dialog.findViewById(R.id.notes_close);
    }

    private EditText labelInput(){ return (EditText) dialog.findViewById(R.id.notesLabelInput); }

    private String labelValue(){ return labelInput().getText().toString(); }

    private EditText valueInput(){ return (EditText) dialog.findViewById(R.id.notesValueInput); }

    private String valueValue(){ return valueInput().getText().toString(); }

    private NotesDialog(Activity activity){
        this.activity = activity;
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.notes_dialog);
        dialog.setTitle("Notes");

        dialog_listener = new NotesDialogListener(dialog);

        // wire up close button
        closeButton().setOnClickListener(new DialogListener(dialog));
    }

    public static Dialog create(Activity activity) {
        return new NotesDialog(activity).dialog;
    }

    NotesDialogListener dialog_listener;

    class NotesDialogListener extends DialogListener{
        NotesDialogListener(Dialog dialog){
            super(dialog);
        }

        public void onClick (View v){
            note_tracker().addNote(labelValue(), valueValue());
            super.onClick(v);
        }
    }
}
