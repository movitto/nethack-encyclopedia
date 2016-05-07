package org.morsi.android.nethack.redux.util;

/**
 * Created by mmorsi on 5/3/16.
 */
public class Input {
    public static boolean validInt(String input){
        // TODO edit_text validation should be a numeric regex
        return !input.equals("") && !input.equals("-");
    }
}
