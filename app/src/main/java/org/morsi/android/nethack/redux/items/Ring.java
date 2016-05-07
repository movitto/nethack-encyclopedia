package org.morsi.android.nethack.redux.items;

import java.util.ArrayList;

public class Ring extends Item {
    public String sink_effect;

    public String wear_effect;

    public static String type(){ return "Potion"; }

    public static Ring extract(String str) {
        String attrs[] = str.split("-");
        Ring ring = new Ring();
        ring.sink_effect = attrs[0];
        ring.wear_effect = attrs[1];
        return ring;
    }

    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = super.compactStringList();
        s.add(sink_effect);
        s.add(wear_effect);
        return s;
    }

    protected ArrayList<String> stringList(){
        ArrayList<String> s = super.stringList();
        s.add(sink_effect);
        s.add(wear_effect);
        return s;
    }
}
