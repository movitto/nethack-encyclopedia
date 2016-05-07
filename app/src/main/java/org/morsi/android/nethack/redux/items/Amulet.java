package org.morsi.android.nethack.redux.items;

import java.util.ArrayList;

public class Amulet extends Item  {
    public String wear_effect;

    public static String type(){ return "Amulet"; }

    public static Amulet extract(String str) {
        String attrs[] = str.split("-");
        Amulet amulet = new Amulet();
        amulet.wear_effect = attrs[0];
        return amulet;
    }

    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = super.compactStringList();
        s.add(wear_effect);
        return s;
    }

    protected ArrayList<String> stringList(){
        ArrayList<String> s = super.stringList();
        s.add(wear_effect);
        return s;
    }
}
