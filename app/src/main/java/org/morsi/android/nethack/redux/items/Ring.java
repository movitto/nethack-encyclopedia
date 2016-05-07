package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;

import org.morsi.android.nethack.redux.util.QuickStatCategory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class Ring extends Item {
    public String sink_effect;

    public String wear_effect;

    public static String type(){ return "Ring"; }

    public String itemType() { return "Ring"; }

    public static String quickStatsCategoryName(){ return "Rings"; }

    public static ArrayList<String> columnNames(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("Ring");
        columns.add("Cost");
        columns.add("Intrinsic");
        columns.add("Notes");
        return columns;
    }

    public static ArrayList<Double> columnWeights(){
        ArrayList<Double> weights = new ArrayList<Double>();
        weights.add(0.3);
        weights.add(0.1);
        weights.add(0.3);
        weights.add(0.3);
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
        columns.add(wear_effect);
        columns.add(notes);
        return columns;
    }
    
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

    public static ArrayList<Ring> fromXML(XmlResourceParser xpp){
        ArrayList<Ring> rings = new ArrayList<Ring>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            Ring current_ring = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if(element_name.equals("ring"))
                        current_ring = new Ring();

                } else if (eventType == XmlPullParser.TEXT) {
                    if(element_name.equals("name"))
                        current_ring.name = xpp.getText();
                    else if(element_name.equals("cost"))
                        current_ring.cost = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("weight"))
                        current_ring.weight = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("probability"))
                        current_ring.probability_str = xpp.getText();
                    else if(element_name.equals("appearance"))
                        current_ring.appearance = xpp.getText();
                    else if(element_name.equals("buy"))
                        current_ring.buy_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("sell"))
                        current_ring.sell_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("sink"))
                        current_ring.sink_effect = xpp.getText();
                    else if(element_name.equals("effect"))
                        current_ring.wear_effect = xpp.getText();
                    else if(element_name.equals("notes"))
                        current_ring.notes = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("ring")) {
                    rings.add(current_ring);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return rings;
    }

    ///

    public static class WearFilter implements Items.filter {
        String effect;

        public WearFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Ring)item).wear_effect.equals(effect);
        }
    };

    public static class SinkFilter implements Items.filter {
        String effect;

        public SinkFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Ring)item).sink_effect.equals(effect);
        }
    };
}
