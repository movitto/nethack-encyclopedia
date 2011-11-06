package org.morsi.android.nethack.redux.util;

import android.content.Context;

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

  // Encyclopedia entry which we are redirecting to (if applicable)
  EncyclopediaEntry redirect_to;

  // Constructor for an encyclopedia entry we have in the catalog
  public EncyclopediaEntry(String rtopic, int rcatalog_number, int rstart_index, int rend_index){
    topic = rtopic; catalog_number = rcatalog_number;
    start_index = rstart_index; end_index = rend_index;
    content = null; redirect_to = null;
  }
  
  // Constructor for an encyclopedia entry we redirect to
  public EncyclopediaEntry(String rtopic, EncyclopediaEntry rRedirectTo){
	  topic = rtopic;
	  content = null;
	  redirect_to = rRedirectTo;
  }

  // Retrieves the content of the entry, pulling it from the appropriate source if neccesary
  public String get_content(Context context){
	  if(redirect_to != null){
		  return redirect_to.get_content(context);
	  }else if(content == null){
		NString catalog_page = Android.assetToNString(context.getAssets(), "encyclopedia/" + Integer.toString(catalog_number));
    	content = catalog_page.substring(start_index, end_index);
    	return content.toString();
    }
	return "Encyclopedia content not found"; // TODO should we raise an exception here?
  }
}
