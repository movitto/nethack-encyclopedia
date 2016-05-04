package org.morsi.android.nethack.redux.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.morsi.android.nethack.redux.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;

public class Tutorial {
	// Retrieve the content stored in the specified android tutorial resource
    public static LinkedHashMap<String, ArrayList<TutorialItem>> getTutorialFromXml(Context context) {
    	LinkedHashMap<String, ArrayList<TutorialItem>> tutorial_content = new LinkedHashMap<String, ArrayList<TutorialItem>>();
        String current_section    = null;
        ArrayList<TutorialItem> current_section_content = null;
        TutorialItem current_item = null;
        
        try{
          XmlResourceParser xpp = context.getResources().getXml(R.xml.tutorial);
          String element_name = ""; int group_id = -1; boolean in_content = false;
          
          int eventType =  xpp.next(); 
          while (eventType != XmlPullParser.END_DOCUMENT){
        	  if(eventType == XmlPullParser.START_TAG){
        		  element_name =  xpp.getName();
        		  if(element_name.equals("section")){
        			  current_section_content = new ArrayList<TutorialItem>();
        			  current_section = xpp.getAttributeValue(null, "name");
            		  group_id = 0;
        			  
        		  }else if(element_name.equals("item")){
        			  current_item = new TutorialItem(group_id++);

        		  }else if(element_name.equals("content")){
					  in_content = true;

				  }else if(in_content)
					  current_item.content += "<" + element_name + ">";
        		  
        	  }else if(eventType == XmlPullParser.TEXT) {
        		  if(element_name.equals("content")){
        			  String content = xpp.getText();
        			  current_item.content = content;

        		  }else if(element_name.equals("image"))
        			  current_item.image = xpp.getText();

				  else if(in_content)
					  current_item.content += xpp.getText();
        		  
        	  }else if(eventType == XmlPullParser.END_TAG){
        		  element_name =  xpp.getName();
        		  if(element_name.equals("section"))
				  	tutorial_content.put(current_section, current_section_content);
        		  else if(element_name.equals("item"))
        			  current_section_content.add(current_item);
				  else if(element_name.equals("content"))
					  in_content = false;
				  else if(in_content)
					  current_item.content += "</" + element_name + ">";
        	  }
        	  
        	  eventType = xpp.next();
          }
        }catch(IOException e) {
        }catch(XmlPullParserException e) {  }
        
        return tutorial_content;
    }
}
