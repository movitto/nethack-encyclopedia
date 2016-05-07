package org.morsi.android.nethack.redux.items;

import java.util.ArrayList;

public class Gem extends Item {
    public String engraving_type;

    public String streak_color;

    public static String type(){ return "Gem"; }

    public static Gem extract(String str) {
        String attrs[] = str.split("-");
        Gem gem = new Gem();
        gem.engraving_type = attrs[0];
        gem.streak_color = attrs[1];
        return gem;
    }

    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = super.compactStringList();
        s.add(engraving_type);
        s.add(streak_color);
        return s;
    }

    protected ArrayList<String> stringList(){
        ArrayList<String> s = super.stringList();
        s.add(engraving_type);
        s.add(streak_color);
        return s;
    }
}
