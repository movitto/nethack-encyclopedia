package org.morsi.android.nethack.redux.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;

import org.morsi.android.nethack.redux.R;

public class LevelDialog {
    Dialog dialog;

    //private Button closeButton(){
    //    return (Button) dialog.findViewById(R.id.level_close);
    //}

    // shop: general, armor, bookstore, liquor, weapons, delicatessan, jewelers, apparel, hardware, rare books, lighting

    // fountain, aligned altar, x-aligned altar, neutral altar, sinks, throne, temple, stash

    // special: oracle, mines (entrance), minetown, mines end, ludios, rogue, quest home, medusa, castle
    //          gehennom, valley, azmodeus, juiblex, baalzebub, orcus-town, wizards tower, fake wizards tower,
    //          vibrating square, moloch's sanctum, vlad's tower

    public static Dialog create(Activity activity) {
        LevelDialog level = new LevelDialog();
        level.dialog = new Dialog(activity);
        //level.dialog.setContentView(R.layout.level_dialog);
        level.dialog.setTitle("Level Properties");

        // wire up close button
        //level.closeButton().setOnClickListener(new DialogListener(notes.dialog));

        return level.dialog;
    }

}
