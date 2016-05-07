package org.morsi.android.nethack.redux.items;


import android.content.res.XmlResourceParser;

import org.morsi.android.nethack.redux.util.QuickStatCategory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class Wand extends Item {
    public String engrave_effect;

    public String zap_effect;

    public int max_charges;

    public String direction;

    public static String type(){ return "Wand"; }

    public String itemType() { return "Wand"; }

    public static String quickStatsCategoryName(){ return "Wands"; }

    public static ArrayList<String> columnNames(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("Wand");
        columns.add("Cost");
        columns.add("Max Charges");
        columns.add("Probability");
        columns.add("Type");
        return columns;
    }

    public static ArrayList<Double> columnWeights(){
        ArrayList<Double> weights = new ArrayList<Double>();
        weights.add(0.32);
        weights.add(0.1);
        weights.add(0.125);
        weights.add(0.125);
        weights.add(0.35);
        return weights;
    }

    public static QuickStatCategory toQuickStatsCategory(){
        QuickStatCategory category = new QuickStatCategory(quickStatsCategoryName());
        category.column_names      = columnNames();
        category.column_weights    = columnWeights();
        return category;
    }

    public ArrayList<String> columns(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add(name);
        columns.add(Integer.toString(cost));
        columns.add(Integer.toString(max_charges));
        columns.add(probability_str);
        columns.add(direction);
        return columns;
    }

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

    public static ArrayList<Wand> fromXML(XmlResourceParser xpp){
        ArrayList<Wand> wands = new ArrayList<Wand>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            Wand current_wand = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if(element_name.equals("wand"))
                        current_wand = new Wand();

                } else if (eventType == XmlPullParser.TEXT) {
                    if(element_name.equals("name"))
                        current_wand.name = xpp.getText();
                    else if(element_name.equals("cost"))
                        current_wand.cost = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("weight"))
                        current_wand.weight = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("probability"))
                        current_wand.probability_str = xpp.getText();
                    else if(element_name.equals("appearance"))
                        current_wand.appearance = xpp.getText();
                    else if(element_name.equals("buy"))
                        current_wand.buy_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("sell"))
                        current_wand.sell_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("engrave"))
                        current_wand.engrave_effect = xpp.getText();
                    else if(element_name.equals("zap"))
                        current_wand.zap_effect = xpp.getText();
                    else if(element_name.equals("max_charges"))
                        current_wand.max_charges = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("direction"))
                        current_wand.direction = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("wand")) {
                    wands.add(current_wand);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return wands;
    }

    ///

    public static class EngravingFilter implements Items.filter {
        String effect;

        public EngravingFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Wand)item).engrave_effect.equals(effect);
        }
    };

    public static class ZapFilter implements Items.filter {
        String effect;

        public ZapFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Wand)item).zap_effect.equals(effect);
        }
    };

}
