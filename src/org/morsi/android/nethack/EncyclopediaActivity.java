/**********************************
 * Nethack Encyclopedia - Encyclopedia Activity
 * 
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/
package org.morsi.android.nethack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.ListActivity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

// to convert wikitext to html
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
import org.morsi.android.nethack.R;

// Provides access to view html based Nethack Encyclopedia articles
public class EncyclopediaActivity extends ListActivity {
		// Store a mapping of topic names to EncylopediaEntry objects
	    public static Map<String, EncyclopediaEntry> topics;
	
	    // Override search to simply bring up the android keyboard.
	    //   Since setTextFilterEnabled is set to true below, typing
	    //   text in this listview will filter the list automatically
	    @Override
	    public boolean onSearchRequested() {
	    	InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    	mgr.showSoftInput(getListView(), 0); // InputMethodManager.SHOW_IMPLICIT to only display if no keyboard is open
	    	return false;
	    }
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
	    	super.onCreate(savedInstanceState);

	    	// parse the list of topics from a registry file we have stored
	    	//  in the project. uses regex groups to extract the topic and
	    	//  the name of the asset file in which the article resides
	    	topics = new TreeMap<String, EncyclopediaEntry>();
	    	ArrayList<String> topic_names = new ArrayList<String>();
	    	String registry = assetToString("encyclopedia/registry");
	    	Pattern pattern = Pattern.compile("(.*):([0-9]*)\n");
	    	Matcher matcher = pattern.matcher(registry);
	    	while(matcher.find()){
	    	   String topic = matcher.group(1);
	    	   int catalog_number = Integer.parseInt(matcher.group(2));
	    	   topics.put(topicToKey(topic), new EncyclopediaEntry(topic, catalog_number));
	    	   topic_names.add(topic);
	    	}
	    	
	    	// automatically wires the list of topic names to
	    	//  list items to display
	    	setListAdapter(new ArrayAdapter<String>(this, R.layout.encyclopedia, topic_names));

	    	// display the list, enable filtering when the user types
	    	//   characters and wire up item click listener
	    	ListView lv = getListView();
	    	lv.setTextFilterEnabled(true);
	    	lv.setOnItemClickListener(new EncyclopediaItemClickedListener());
	    }    
	    
	    // helper to read the specified android asset file into a string
	    private String assetToString(String asset){	
	    	try{
		    	Writer writer = new StringWriter();
		    	AssetManager assetManager = getAssets();
	    		InputStream in = assetManager.open(asset);
	    		Reader reader = new BufferedReader(new InputStreamReader(in));
	    		int n; char[] buffer = new char[1024];
	    		while ((n = reader.read(buffer)) != -1) {
	    		  writer.write(buffer, 0, n);
	    		}
	    		return writer.toString();
	    	} catch (IOException e) {
	    		return "";
	    	}
	    }
	    
	    // helper to convert topic name to key
	    private static String topicToKey(String topic){
	    	return topic.toLowerCase().replaceAll("\\s",	"_");
	    }
	    
	    // Encapsulates an encyclopedia article
	    private class EncyclopediaEntry {
	    	// Title of the article
	    	public String topic;
	    	
	    	// Asset file number which this article resides
	    	public int    catalog_number;
	    	
	    	// Actual content of the article
	    	public String content;
	    	
	    	// Indicates if the article has been processed
	    	//  after it has been read from the file
	    	public boolean converted;
	    	
	    	// Indicates if this article redirects to another
	    	public boolean is_redirect;
	    	
	    	// Title of the article this one redirects to
	    	public String redirect_to;
	    	
	    	public EncyclopediaEntry(String rtopic, int rcatalog_number){
	    		topic = rtopic; catalog_number = rcatalog_number;
	    		converted = false; is_redirect = false;
	    	}
	    	
	    	// Pulls article content from the asset file on which it resides,
	    	//   populating the EncyclopediaEntry for all other articles residing 
	    	//   on that file as well
	    	public void populateContent(){
	    		if(content == null){
					try {
						// Use xpath to traverse the catalog page
						String catalog_page = assetToString("encyclopedia/" + Integer.toString(catalog_number) + ".xml");
						XPath xpath = XPathFactory.newInstance().newXPath();
						InputSource inputSource = new InputSource(new StringReader(catalog_page));
						// replace this xpath w/ a sax traversal for performance ?
						NodeList nodeList = (NodeList) xpath.evaluate("/pages/page", inputSource, XPathConstants.NODESET);
						for(int i=0; i<nodeList.getLength(); i++){ 
						  Node childNode = nodeList.item(i);
						  String ntopic = childNode.getAttributes().getNamedItem("name").getNodeValue();
						  EncyclopediaActivity.topics.get(topicToKey(ntopic)).content = childNode.getTextContent();
						}
					} catch (XPathExpressionException e) {}
				}
	    	}
	    	
	    	// Covert entry into its final presentation format if not already done.
	    	public void convertContent(){
	    		if(!converted){    		
	    			converted = true;
	    			
	    			// if we are redirecting, parse redirect_to location
	    			if(content.substring(2, 10).equalsIgnoreCase("redirect")){
	    				is_redirect = true;
	    				redirect_to = content.substring(10, content.length()-1).
	    				                      replaceFirst("\\s", "").
	    				                      replaceFirst("\\[\\[", "").
	    				                      replaceFirst("\\]\\]", "");    				
	    				return;
	    			}
	    			
	    			// convert wikitext content to html using org.eclipse.mylyn.wikitext
			    	StringWriter html_writer     = new StringWriter(); //A character stream that collects its output in a string buffer, which can then be used to construct a string.
		    	    HtmlDocumentBuilder builder  = new HtmlDocumentBuilder(html_writer); //Builds the model for a view
		    	    MediaWikiLanguage language   = new MediaWikiLanguage(); //Class is explained as "A textile dialect that parses Textile markup."
		    	    MarkupParser wikitext_parser = new MarkupParser(language, builder);
					wikitext_parser.parse(content);
					
					// encode the article as utf8
	    			// TODO pre-encode content in parser
	    			try { content = URLEncoder.encode(html_writer.toString(), "utf-8").replaceAll("\\+"," ");
					} catch (UnsupportedEncodingException e) {}
				}
	    	}
	    }
	    
	    // Handles encyclopedia article clicks
	    private class EncyclopediaItemClickedListener implements OnItemClickListener {   	
	    	public EncyclopediaItemClickedListener(){
	    		super();
	    	}
	    	
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String topic = ((TextView) view).getText().toString();
				EncyclopediaEntry entry = EncyclopediaActivity.topics.get(topicToKey(topic));
	    	    initiatePopupWindow(entry);
			}
	    }
	    
	    // Show popup window when encyclopedia page is clicked
	    private void initiatePopupWindow(EncyclopediaEntry entry) {
	        try {
	        	// TODO display error if entry is null
	            LayoutInflater inflater = (LayoutInflater) EncyclopediaActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            View layout = inflater.inflate(R.layout.encyclopedia_popup,
	                    (ViewGroup) findViewById(R.id.encyclopedia_page_popup));
	            PopupWindow pw = new PopupWindow(layout, 300, 470, true);
	     
	            TextView text = (TextView) layout.findViewById(R.id.encyclopedia_page_title);
	            text.setText(entry.topic);
	            WebView web_view = (WebView) layout.findViewById(R.id.encyclopedia_page_content);
	            
	            // intercept link clicks, redirect to our encyclopedia
	            web_view.setWebViewClient(new WebViewClient() {
	            	@Override  
	                public boolean shouldOverrideUrlLoading(WebView view, String url)  
	                {  
	            		if(url.substring(0, 6).equals("/wiki/")){
							String new_topic = url.substring(6, url.length());
	            			EncyclopediaEntry new_entry = EncyclopediaActivity.topics.get(topicToKey(new_topic));
	            			initiatePopupWindow(new_entry);
	            			return true; 
	            		}
	            		return false;
	                }
	            });  
				entry.populateContent(); entry.convertContent();
				
				if(!entry.is_redirect){
					// if we are not redicting, display page
					// http://code.google.com/p/android-rss/issues/detail?id=15
					web_view.loadData(entry.content,  "text/html", "utf-8");
					pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
				}else{
					EncyclopediaEntry new_entry = EncyclopediaActivity.topics.get(topicToKey(entry.redirect_to));
					initiatePopupWindow(new_entry);
				}
	            
	            Button cancelButton = (Button) layout.findViewById(R.id.encyclopedia_page_close);
	            cancelButton.setOnClickListener(new EncyclopediaPageClosedListener(pw));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	     
	    // Handles clicks to the closed button on the encyclopedia page
	    private class EncyclopediaPageClosedListener implements Button.OnClickListener {
	    	private PopupWindow popup_window;
	    	
	    	public EncyclopediaPageClosedListener(PopupWindow lpw){
	    		popup_window = lpw;
	    	}
			public void onClick(View v) {
				popup_window.dismiss();
			}
	    };
	    
}
