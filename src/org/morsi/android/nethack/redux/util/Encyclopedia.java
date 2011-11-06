package org.morsi.android.nethack.redux.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.morsi.android.nethack.redux.EncyclopediaActivity;

public class Encyclopedia {
    static final byte REGISTRY_DELIM = 5;
    
    public static final String symbol_string = "!@%*";
    public static final String number_string = "#";

    // mapping of topic names to encyclopedia entries
    public Map<String, EncyclopediaEntry> topics;
  
    // mapping of encyclopedia section names to lists of topic names for fast lookup
    public Map<String, ArrayList<String>> topic_names;

    public static String topicToKey(String rtopic){
        return rtopic.toLowerCase().replaceAll("\\s", "_");
      }
    
    public Encyclopedia(){
      topics = new HashMap<String, EncyclopediaEntry>();
      topic_names = new HashMap<String, ArrayList<String>>();
      for(String section : EncyclopediaActivity.alphabet_sections)
    	  topic_names.put(section, new ArrayList<String>());
    }

    public EncyclopediaEntry get(String topic){
      return topics.get(Encyclopedia.topicToKey(topic));
    }
    
    public ArrayList<String> topicNames(String starting_with){
  	  return topic_names.get(starting_with.toLowerCase());
    }

    // Pull the list of topics out of the registry and redirects files
    public void retrieveTopics(NString registry, NString redirects){
      // used to determine which topic_names section topic should go under
      Pattern symbollic_pattern = Pattern.compile("^[^[0-9a-zA-Z]].*");	
      Pattern numeric_pattern   = Pattern.compile("^[0-9].*");
      Pattern alpha_pattern     = Pattern.compile("^([a-zA-Z]).*", Pattern.CASE_INSENSITIVE);
      
      // iterate through the registry, one byte at a time
      for(int i = 0, s = 0; i < registry.length(); ++i){
        if(registry.byteAt(i) == REGISTRY_DELIM){
          // pull out topic, catalog page, start index, end index
		  String topic = registry.substring(s, i).toString();
          int i1 = registry.indexOf(REGISTRY_DELIM, i+1),
              i2 = registry.indexOf(REGISTRY_DELIM, i1+1),
              i3 = registry.indexOf(REGISTRY_DELIM, i2+1);
          String s1 = registry.substring(i+1,  i1).toString(),
                 s2 = registry.substring(i1+1, i2).toString(),
                 s3 = registry.substring(i2+1, i3).toString();
          
          // create new encyclopedia entry
          EncyclopediaEntry e = new EncyclopediaEntry(topic,
              Integer.parseInt(s1),
              Integer.parseInt(s2),
              Integer.parseInt(s3));
          
          // add it to topics and appropriate topic_names section
          topics.put(Encyclopedia.topicToKey(e.topic), e);
          if(symbollic_pattern.matcher(topic).find())    topic_names.get(Encyclopedia.symbol_string).add(topic);
          else if(numeric_pattern.matcher(topic).find()) topic_names.get(Encyclopedia.number_string).add(topic);
          else{
        	  Matcher matcher = alpha_pattern.matcher(topic);
        	  if(matcher.find()) topic_names.get(matcher.group(1).toLowerCase()).add(topic);
          }
          
	      s = i3+1; i = i3 + 1;
		}
      }
      
      // iterate of the redirects one byte at a time
      for(int i = 0, s = 0; i < redirects.length(); ++i){
          if(redirects.byteAt(i) == REGISTRY_DELIM){
        	  // pull out topic
        	  String topic = redirects.substring(s, i).toString();
        	  
        	  // pull out what it redirects to
        	  int i1 = redirects.indexOf(REGISTRY_DELIM, i+1);
        	  String s1 = redirects.substring(i+1,  i1).toString();
        	  
        	  // lookup entry it redirects to
        	  EncyclopediaEntry redirect = topics.get(Encyclopedia.topicToKey(s1));
        	  
        	  // create new encyclopedia entry
        	  EncyclopediaEntry e = new EncyclopediaEntry(topic, redirect);
        	  
        	  // add it to topics 
        	  // for now don't store it in topic_names so they don't appear in master encyclopdia list
              topics.put(Encyclopedia.topicToKey(e.topic), e);
              /*if(symbollic_pattern.matcher(topic).find())    topic_names.get(Encyclopedia.symbol_string).add(topic);
              else if(numeric_pattern.matcher(topic).find()) topic_names.get(Encyclopedia.number_string).add(topic);
              else{
            	  Matcher matcher = alpha_pattern.matcher(topic);
            	  if(matcher.find()) topic_names.get(matcher.group(1).toLowerCase()).add(topic);
              }*/
              
    	      s = i1+1; i = i1 + 1;
          }
      }
    }
}
