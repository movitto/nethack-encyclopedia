package org.morsi.android.nethack.redux.util;

import android.content.res.AssetManager;

//Encapsulates an encyclopedia article
public class EncyclopediaEntry {
  // Title of the article
  public String topic;

  // Asset file number which this article resides
  public int    catalog_number;

  // Location in the catalog which the articles resides
  public int start_index;

  // Location in the catalog which the article ends
  public int end_index;

  // Actual content of the article
  public NString content;

  // Indicates if the article has been processed
  //  after it has been read from the file
  public boolean converted;

  public EncyclopediaEntry(String rtopic, int rcatalog_number, int rstart_index, int rend_index){
    topic = rtopic; catalog_number = rcatalog_number;
    start_index = rstart_index; end_index = rend_index;
    converted = false;
  }

  public static String topicToKey(String rtopic){
    return rtopic.toLowerCase().replaceAll("\\s", "_");
  }

  // Pulls article content from the string catalog page which it resides, returns reference to this
  public EncyclopediaEntry populate(NString catalog_page){
    if(content == null){
    	content = catalog_page.substring(start_index, end_index);
    }
    return this;
  }
  
  // First generates the catalog page then pulls article content from it
  public EncyclopediaEntry populate(AssetManager assets){
	  NString catalog_page = Android.assetToNString(assets, "encyclopedia/" + Integer.toString(catalog_number));
	  populate(catalog_page);
	  return this;
  }
}
