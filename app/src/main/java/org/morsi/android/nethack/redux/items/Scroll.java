package org.morsi.android.nethack.redux.items;

import java.util.ArrayList;

public class Scroll extends Item {
    public String read_effect;

    public String drop_effect;

    public static String type(){ return "Scroll"; }

    public static Scroll extract(String str) {
        String attrs[] = str.split("-");
        Scroll scroll = new Scroll();
        scroll.read_effect = attrs[0];
        scroll.drop_effect = attrs[1];
        return scroll;
    }

    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = super.compactStringList();
        s.add(read_effect);
        s.add(drop_effect);
        return s;
    }

    protected ArrayList<String> stringList(){
        ArrayList<String> s = super.stringList();
        s.add(read_effect);
        s.add(drop_effect);
        return s;
    }
}
