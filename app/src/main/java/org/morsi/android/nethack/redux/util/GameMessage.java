package org.morsi.android.nethack.redux.util;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.morsi.android.nethack.redux.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class GameMessage {
    public String game;
    public String meaning;

    public static ArrayList<GameMessage> getMessagesFromXml(Context context){
        ArrayList<GameMessage> results = new ArrayList<GameMessage>();

        try {
            XmlResourceParser xpp = context.getResources().getXml(R.xml.game_messages);
            String element_name = ""; GameMessage message = null;

            int eventType =  xpp.next();
            while (eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG) {
                    element_name =  xpp.getName();
                    if(element_name.equals("message"))
                        message = new GameMessage();
                }else if(eventType == XmlPullParser.TEXT) {
                    if(element_name.equals("game") && message != null)
                        message.game = xpp.getText();
                    else if(element_name.equals("meaning") && message != null)
                        message.meaning = xpp.getText();
                }else if(eventType == XmlPullParser.END_TAG){
                    element_name =  xpp.getName();
                    if(element_name.equals("message"))
                        results.add(message);
                }

                eventType = xpp.next();
            }
        }catch(IOException e) {
        }catch(XmlPullParserException e) {  }

        return results;
    }
}
