package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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

        public static ArrayList<Amulet> fromXML(XmlResourceParser xpp) {
        ArrayList<Amulet> amulets = new ArrayList<Amulet>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            Amulet current_amulet = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if (element_name.equals("amulet"))
                        current_amulet = new Amulet();

                } else if (eventType == XmlPullParser.TEXT) {
                    if (element_name.equals("name"))
                        current_amulet.name = xpp.getText();
                    else if (element_name.equals("cost"))
                        current_amulet.cost = Integer.parseInt(xpp.getText());
                    else if (element_name.equals("weight"))
                        current_amulet.weight = Integer.parseInt(xpp.getText());
                    else if (element_name.equals("probability"))
                        current_amulet.probability_str = xpp.getText();
                    else if (element_name.equals("appearance"))
                        current_amulet.appearance = xpp.getText();
                    else if (element_name.equals("buy"))
                        current_amulet.buy_price = Integer.parseInt(xpp.getText());
                    else if (element_name.equals("sell"))
                        current_amulet.sell_price = Integer.parseInt(xpp.getText());
                    else if (element_name.equals("wear"))
                        current_amulet.wear_effect = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("amulet")) {
                    amulets.add(current_amulet);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return amulets;
    }

    ///

    public static class WearFilter implements Items.filter {
        String effect;

        public WearFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Amulet)item).wear_effect.equals(effect);
        }
    };
}
