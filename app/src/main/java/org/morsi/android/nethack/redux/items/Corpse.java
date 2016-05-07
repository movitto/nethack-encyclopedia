package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class Corpse extends Item{
    public int nutrition;

    public String initial_effect;

    public String final_effect;

    public String intrinsic;

    public static String type(){ return "Corpse"; }

    public static ArrayList<String> columnNames(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("Name");
        columns.add("Nutrtion");
        columns.add("Initial Effect");
        columns.add("Final Effect");
        columns.add("Intrinsic");
        return columns;
    }

    public static ArrayList<Double> columnWeights(){
        ArrayList<Double> weights = new ArrayList<Double>();
        weights.add(0.25);
        weights.add(0.15);
        weights.add(0.15);
        weights.add(0.15);
        weights.add(0.30);
        return weights;
    }

    public ArrayList<String> columns() {
        ArrayList<String> columns = new ArrayList<String>();
        columns.add(name);
        columns.add(Integer.toString(nutrition));
        columns.add(initial_effect);
        columns.add(final_effect);
        columns.add(intrinsic);
        return columns;
    }

    public static ArrayList<Corpse> fromXML(XmlResourceParser xpp) {
        ArrayList<Corpse> corpses = new ArrayList<Corpse>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            Corpse current_corpse = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if (element_name.equals("corpse"))
                        current_corpse = new Corpse();

                } else if (eventType == XmlPullParser.TEXT) {
                    if (element_name.equals("name"))
                        current_corpse.name = xpp.getText();
                    else if (element_name.equals("weight"))
                        current_corpse.weight = Integer.parseInt(xpp.getText());
                    else if (element_name.equals("nutrition"))
                        current_corpse.nutrition = Integer.parseInt(xpp.getText());
                    else if (element_name.equals("initial_effect"))
                        current_corpse.initial_effect = xpp.getText();
                    else if (element_name.equals("final_effect"))
                        current_corpse.final_effect = xpp.getText();
                    else if (element_name.equals("intrinsic"))
                        current_corpse.intrinsic = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("corpse")) {
                    corpses.add(current_corpse);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return corpses;
    }
}
