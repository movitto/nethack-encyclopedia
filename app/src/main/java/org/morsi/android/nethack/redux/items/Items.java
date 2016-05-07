package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;

import java.util.ArrayList;

public class Items extends ArrayList<Item> {
    public static Items fromXML(XmlResourceParser xpp){
        Items items = new Items();
        // ...
        return items;
    }

    public interface filter{
        boolean matches(Item item);
    }

    public Items filter(filter f){
        Items result = new Items();
        for(Item i : this)
            if(f.matches(i))
                result.add(i);
        return result;
    }

    public ArrayList<String> names(){
        ArrayList<String> names = new ArrayList<String>();
        for(Item i : this)
            names.add(i.name);
        return names;
    }
}
