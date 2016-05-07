package org.morsi.android.nethack.redux.items;

import java.util.ArrayList;

public class SpellBook extends Item {
    public static String type(){ return "SpellBook"; }

    public static SpellBook extract(String str) {
        return new SpellBook();
    }
}
