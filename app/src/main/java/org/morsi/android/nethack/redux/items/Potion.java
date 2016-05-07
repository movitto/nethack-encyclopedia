package org.morsi.android.nethack.redux.items;

import android.text.TextUtils;

import java.util.ArrayList;

public class Potion extends Item {
    public String quaff_effect;

    public String throw_effect;

    public static String type(){ return "Potion"; }

    public static Potion extract(String str) {
        String attrs[] = str.split("-");
        Potion potion = new Potion();
        potion.quaff_effect = attrs[0];
        potion.throw_effect = attrs[1];
        return potion;
    }

    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = super.compactStringList();
        s.add(quaff_effect);
        s.add(throw_effect);
        return s;
    }

    protected ArrayList<String> stringList(){
        ArrayList<String> s = super.stringList();
        s.add(quaff_effect);
        s.add(throw_effect);
        return s;
    }
}
