package org.morsi.android.nethack.redux.dialogs;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;

// Handles clicks to dialog close buttons
class DialogListener implements Button.OnClickListener {
    Dialog dialog_to_close;

    DialogListener(Dialog dialog) {
        dialog_to_close = dialog;
    }

    public void onClick(View v) {
        dialog_to_close.dismiss();
    }
}
