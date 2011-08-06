package org.morsi.android.nethack.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Encyclopedia {
    static final char REGISTRY_DELIM = 5;
    static String registry;

  public Map<String, EncyclopediaEntry> topics;
  public ArrayList<String> topic_names;

  public Encyclopedia(){
    topics = new HashMap<String, EncyclopediaEntry>();
    topic_names = new ArrayList<String>();
  }

  // Pull the list of topics out of the registry file
  public void parseTopics(String rregistry){
    registry = rregistry;
    for(int i = 0, d = 0, s = 0; i < registry.length(); ++i){
        char c = registry.charAt(i);
        if(c == REGISTRY_DELIM){
          if((d++) % 4 == 0) add_topic(registry.substring(s, i));
            s = i+1;
        }
      }
  }

  // Pull a single topic out of the registry file
  public EncyclopediaEntry parseTopic(String topic){
    EncyclopediaEntry e = get(EncyclopediaEntry.topicToKey(topic));
    if(e == null){
      for(int i = 0, s = 0; i < registry.length(); ++i){
          if(registry.charAt(i) == REGISTRY_DELIM){
            if(registry.subSequence(s, i).equals(topic)){
              int i1 = registry.indexOf(REGISTRY_DELIM, i+1),
                  i2 = registry.indexOf(REGISTRY_DELIM, i1+1),
                  i3 = registry.indexOf(REGISTRY_DELIM, i2+1);
              String s1 = registry.substring(i+1, i1),
                     s2 = registry.substring(i1+1, i2),
                     s3 = registry.substring(i2+1, i3);
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
    topic_names.add(topic);
  }

  public EncyclopediaEntry get(String topic){
    return topics.get(topic);
  }

  public EncyclopediaEntry lookup(String topic){
    return parseTopic(topic);
  }

  public ArrayList<String> topicNames(){
    return topic_names;
  }
}
