package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class Scroll extends Item {
    public String read_effect;

    public String drop_effect;

    public int ink;

    public static String type(){ return "Scroll"; }

    public static ArrayList<String> columnNames(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("Scroll");
        columns.add("Cost");
        columns.add("Weight");
        columns.add("Probability");
        columns.add("Ink");
        columns.add("Appearance");
        return columns;
    }

    public static ArrayList<Double> columnWeights(){
        ArrayList<Double> weights = new ArrayList<Double>();
        weights.add(0.32);
        weights.add(0.15);
        weights.add(0.15);
        weights.add(0.13);
        weights.add(0.25);
        return weights;
    }

    public ArrayList<String> columns(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add(name);
        columns.add(Integer.toString(cost));
        columns.add(Integer.toString(weight));
        columns.add(probability_str);
        columns.add(Integer.toString(ink));
        columns.add(appearance);
        return columns;
    }

    public static Scroll extract(String str) {
        String attrs[] = str.split("-");
        Scroll scroll = new Scroll();
        scroll.read_effect = attrs[0];
        scroll.drop_effect = attrs[1];
        return scroll;
    }

    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = super.compactStringList();
        s.add(read_effect);
        s.add(drop_effect);
        return s;
    }

    protected ArrayList<String> stringList(){
        ArrayList<String> s = super.stringList();
        s.add(read_effect);
        s.add(drop_effect);
        return s;
    }

    public static ArrayList<Scroll> fromXML(XmlResourceParser xpp){
        ArrayList<Scroll> scrolls = new ArrayList<Scroll>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            Scroll current_scroll = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if(element_name.equals("scroll"))
                        current_scroll = new Scroll();

                } else if (eventType == XmlPullParser.TEXT) {
                    if(element_name.equals("name"))
                        current_scroll.name = xpp.getText();
                    else if(element_name.equals("cost"))
                        current_scroll.cost = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("weight"))
                        current_scroll.weight = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("probability"))
                        current_scroll.probability_str = xpp.getText();
                    else if(element_name.equals("appearance"))
                        current_scroll.appearance = xpp.getText();
                    else if(element_name.equals("buy"))
                        current_scroll.buy_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("sell"))
                        current_scroll.sell_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("read"))
                        current_scroll.read_effect = xpp.getText();
                    else if(element_name.equals("drop"))
                        current_scroll.drop_effect = xpp.getText();
                    else if(element_name.equals("ink"))
                        current_scroll.ink = Integer.parseInt(xpp.getText());

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("scroll")) {
                    scrolls.add(current_scroll);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return scrolls;
    }

    ///

    public static class ReadFilter implements Items.filter {
        String effect;

        public ReadFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Scroll)item).read_effect.equals(effect);
        }
    };

    public static class DropFilter implements Items.filter {
        String effect;

        public DropFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Scroll)item).drop_effect.equals(effect);
        }
    };
}
