package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class SpellBook extends Item {
    public static String type(){ return "SpellBook"; }

    public static SpellBook extract(String str) {
        return new SpellBook();
    }

    public static ArrayList<SpellBook> fromXML(XmlResourceParser xpp) {
        ArrayList<SpellBook> spellbooks = new ArrayList<SpellBook>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            SpellBook current_spellbook = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if (element_name.equals("spellbook"))
                        current_spellbook = new SpellBook();

                } else if (eventType == XmlPullParser.TEXT) {
                    if (element_name.equals("name"))
                        current_spellbook.name = xpp.getText();
                    else if (element_name.equals("cost"))
                        current_spellbook.cost = Integer.parseInt(xpp.getText());
                    else if (element_name.equals("weight"))
                        current_spellbook.weight = Integer.parseInt(xpp.getText());
                    else if (element_name.equals("probability"))
                        current_spellbook.probability_str = xpp.getText();
                    else if (element_name.equals("appearance"))
                        current_spellbook.appearance = xpp.getText();
                    else if (element_name.equals("buy"))
                        current_spellbook.buy_price = Integer.parseInt(xpp.getText());
                    else if (element_name.equals("sell"))
                        current_spellbook.sell_price = Integer.parseInt(xpp.getText());
                    
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("spellbook")) {
                    spellbooks.add(current_spellbook);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return spellbooks;
    }
}
