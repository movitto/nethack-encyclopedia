package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class Armor extends Item{
    public int ac;

    public String material;

    public String wear_effect;

    public int mc;

    public boolean magic;

    public static String type(){ return "Armor"; }

    public static ArrayList<String> columnNames(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("Name");
        columns.add("$");
        columns.add("Wgt");
        columns.add("AC");
        columns.add("Mat");
        columns.add("Effect");
        columns.add("MC");
        columns.add("Mag");
        columns.add("Appearance");
        return columns;
    }

    public static ArrayList<Double> columnWeights(){
        ArrayList<Double> weights = new ArrayList<Double>();
        weights.add(0.25);
        weights.add(0.06);
        weights.add(0.06);
        weights.add(0.05);
        weights.add(0.13);
        weights.add(0.1);
        weights.add(0.05);
        weights.add(0.06);
        weights.add(0.24);
        return weights;
    }

    public ArrayList<String> columns(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add(name);
        columns.add(Integer.toString(cost));
        columns.add(Integer.toString(weight));
        columns.add(Integer.toString(ac));
        columns.add(material);
        columns.add(wear_effect);
        columns.add(Integer.toString(mc));
        columns.add(Boolean.toString(magic));
        columns.add(appearance);
        return columns;
    }

    public static ArrayList<Armor> fromXML(XmlResourceParser xpp){
        ArrayList<Armor> armors = new ArrayList<Armor>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            Armor current_armor = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if(element_name.equals("armor"))
                        current_armor = new Armor();

                } else if (eventType == XmlPullParser.TEXT) {
                    if(element_name.equals("name"))
                        current_armor.name = xpp.getText();
                    else if(element_name.equals("cost"))
                        current_armor.cost = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("weight"))
                        current_armor.weight = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("probability"))
                        current_armor.probability_str = xpp.getText();
                    else if(element_name.equals("appearance"))
                        current_armor.appearance = xpp.getText();
                    else if(element_name.equals("buy"))
                        current_armor.buy_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("sell"))
                        current_armor.sell_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("ac"))
                        current_armor.ac = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("material"))
                        current_armor.material = xpp.getText();
                    else if(element_name.equals("effect"))
                        current_armor.wear_effect = xpp.getText();
                    else if(element_name.equals("mc"))
                        current_armor.mc = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("magic"))
                        current_armor.magic = Boolean.parseBoolean(xpp.getText());

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("armor")) {
                    armors.add(current_armor);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return armors;
    }
}
