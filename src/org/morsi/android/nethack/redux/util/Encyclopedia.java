package org.morsi.android.nethack.redux.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Encyclopedia {
    static final byte REGISTRY_DELIM = 5;
    static NString registry;
    
    public static final String symbol_string = "!@%*";
    public static final String number_string = "#";

    // mapping of topic names to encyclopedia entries
    public Map<String, EncyclopediaEntry> topics;
  
    // mapping of encyclopedia section names to lists of topic names 
    public Map<String, ArrayList<String>> topic_names;

  public Encyclopedia(){
    topics = new HashMap<String, EncyclopediaEntry>();
    topic_names = new HashMap<String, ArrayList<String>>();
  }

  // Pull the list of topics out of the registry file
  public void parseTopics(NString rregistry){
    registry = rregistry;
    for(int i = 0, d = 0, s = 0; i < registry.length(); ++i){
        byte c = registry.byteAt(i);
        if(c == REGISTRY_DELIM){
          if((d++) % 4 == 0) add_topic(registry.substring(s, i).toString());
            s = i+1;
        }
      }
  }

  // Pull a single topic out of the registry file
  public EncyclopediaEntry parseTopic(String topic){
    EncyclopediaEntry e = get(EncyclopediaEntry.topicToKey(topic));
    if(e == null){
      for(int i = 0, s = 0; i < registry.length(); ++i){
          if(registry.byteAt(i) == REGISTRY_DELIM){
            if(registry.substring(s, i).toString().equals(topic)){
              int i1 = registry.indexOf(REGISTRY_DELIM, i+1),
                  i2 = registry.indexOf(REGISTRY_DELIM, i1+1),
                  i3 = registry.indexOf(REGISTRY_DELIM, i2+1);
              String s1 = registry.substring(i+1,  i1).toString(),
                     s2 = registry.substring(i1+1, i2).toString(),
                     s3 = registry.substring(i2+1, i3).toString();
              e = new EncyclopediaEntry(topic,
                  Integer.parseInt(s1),
                  Integer.parseInt(s2),
                  Integer.parseInt(s3));
              add(e);
              break;
            }
            s = i+1;
          }
      }
    }
    return e;
  }

  public void add(EncyclopediaEntry e){
    topics.put(EncyclopediaEntry.topicToKey(e.topic), e);
  }

  public void add_topic(String topic){
	  String section = null;
	  Pattern pattern = Pattern.compile("^[^[0-9a-zA-Z]].*");
	  Matcher matcher = pattern.matcher(topic);
	  if(matcher.find()) section = Encyclopedia.symbol_string;
	  
	  if(section == null){
		  pattern = Pattern.compile("^[0-9].*");
		  matcher = pattern.matcher(topic);
		  if(matcher.find()) section = Encyclopedia.number_string;
	  }
	  
	  if(section == null){
		  pattern = Pattern.compile("^([a-zA-Z]).*", Pattern.CASE_INSENSITIVE);
		  matcher = pattern.matcher(topic);
		  if(matcher.find()) section = matcher.group(1);
	  }
	  
	  if(section != null){ // TODO what to do if it is?
		  if(!topic_names.containsKey(section.toLowerCase())) topic_names.put(section.toLowerCase(), new ArrayList<String>());
		  topic_names.get(section.toLowerCase()).add(topic);
	  }
  }

  public EncyclopediaEntry get(String topic){
    return topics.get(topic);
  }

  public EncyclopediaEntry lookup(String topic){
    return parseTopic(topic);
  }

  public ArrayList<String> topicNames(){
	  ArrayList<String> res = new ArrayList<String>();
	  for(String section : topic_names.keySet())
		  for(String topic_name : topic_names.get(section))
			  res.add(topic_name);
    return res;
  }
  
  public ArrayList<String> topicNames(String starting_with){
	  return topic_names.get(starting_with.toLowerCase());
  }
}
