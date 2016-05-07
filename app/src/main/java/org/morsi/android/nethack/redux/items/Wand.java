package org.morsi.android.nethack.redux.items;


import java.util.ArrayList;

public class Wand extends Item {
    public String engrave_effect;

    public String zap_effect;

    public static String type(){ return "Wand"; }

    public static Wand extract(String str) {
        String attrs[] = str.split("-");
        Wand wand = new Wand();
        wand.engrave_effect = attrs[0];
        wand.zap_effect = attrs[1];
        return wand;
    }

    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = super.compactStringList();
        s.add(engrave_effect);
        s.add(zap_effect);
        return s;
    }

    protected ArrayList<String> stringList(){
        ArrayList<String> s = super.stringList();
        s.add(engrave_effect);
        s.add(zap_effect);
        return s;
    }
}
