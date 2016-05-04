package org.morsi.android.nethack.redux.dialogs;

import android.app.Activity;
import android.app.Dialog;

/**
 * Created by mmorsi on 5/3/16.
 */
public class ItemDialog {
    Dialog dialog;

    // all: id, appearance (customized), buy price, sell price
    // output: specified attrs, possible identifications

    // wands
    // potions
    // scrolls
    // spellbooks
    // rings
    // amulets
    // gems

    public static Dialog create(Activity activity) {
        ItemDialog item = new ItemDialog();
        item.dialog = new Dialog(activity);
        //item.dialog.setContentView(R.layout.item_dialog);
        //item.dialog.setTitle("");

        // wire up close button
        //item.closeButton().setOnClickListener(new DialogListener(notes.dialog));

        return item.dialog;
    }

}
