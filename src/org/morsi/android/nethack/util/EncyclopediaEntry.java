package org.morsi.android.nethack.util;

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
  public String content;

  // Indicates if the article has been processed
  //  after it has been read from the file
  public boolean converted;

  // Indicates if this article redirects to another
  public boolean is_redirect;

  // Title of the article this one redirects to
  public String redirect_to;

  public EncyclopediaEntry(String rtopic, int rcatalog_number, int rstart_index, int rend_index){
    topic = rtopic; catalog_number = rcatalog_number;
    start_index = rstart_index; end_index = rend_index;
    converted = false; is_redirect = false;
  }

  public static String topicToKey(String rtopic){
    return rtopic.toLowerCase().replaceAll("\\s", "_");
  }

  // Pulls article content from the string catalog page which it resides
  public void populateContent(String catalog_page){
    if(content == null) content = catalog_page.substring(start_index, end_index);
    // FIXME set is_redirect and redirect_to
  }
}
