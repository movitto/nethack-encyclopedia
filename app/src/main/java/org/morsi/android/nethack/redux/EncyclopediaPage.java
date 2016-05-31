
package org.morsi.android.nethack.redux;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.EncyclopediaEntry;

// Show encyclopedia page screen when encyclopedia topic is clicked
public class EncyclopediaPage extends Activity
{
    // stack of pages which have been displayed (To restore in order via back button)
    private static Stack<String> page_stack = new Stack<String>();
    
    // corresponding stack of page anchors
    private static Stack<String> anchors_stack = new Stack<String>();

    // show a new topic via the encyclopedia page
    public static void showPage(Context context, String topic){
        if(page_stack.isEmpty() || !page_stack.peek().equals(topic)){
            page_stack.push(topic);
            anchors_stack.push("");
        }
        Intent EncPage = new Intent(context, EncyclopediaPage.class);
        EncPage.putExtra("page", topic);
        EncPage.putExtra("anchor", anchors_stack.peek());
        context.startActivity(EncPage);
    }

    // close current page
    public void closePage(){
        setResult(RESULT_OK);
        finish();
        if(!page_stack.isEmpty())
            EncyclopediaPage.showPage(this,	page_stack.peek());
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final String current_page = getIntent().getExtras().getString("page");
        final String current_anchor = getIntent().getExtras().getString("anchor");

        setContentView(R.layout.encyclopedia_popup);
        Button close_button = (Button) findViewById(R.id.encyclopedia_page_close);
        final EncyclopediaEntry entry = EncyclopediaActivity.encyclopedia.get(current_page);

        TextView title = (TextView) findViewById(R.id.encyclopedia_page_title);
        final WebView web_view = (WebView) findViewById(R.id.encyclopedia_page_content);

        // display error if entry is null
        if(entry == null){
            title.setText("Page " + current_page + " not found");
        }else{
	        title.setText(entry.topic);

	        // intercept link clicks, redirect to our encyclopedia
	        web_view.setWebViewClient(new WebViewClient() {
	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url)
	            {
	                if(url.startsWith("file:///")){
	            		int end = url.indexOf('#');
	                    if(end == -1) end = url.length();

	                    String next_page = url.substring(8, end); // remove file:///
	                    String anchor = (end == url.length() ? "" : url.substring(end, url.length()));
	                    page_stack.push(next_page);
	                    anchors_stack.push(anchor);
	                    closePage();
	                    return true;
	                }
	                return false;
	           }
	        });

            // need to use loadDataWithBaseURL & valid base url:
            // http://code.google.com/p/android-rss/issues/detail?id=15
            web_view.loadDataWithBaseURL("file:///" + current_page + current_anchor,
                    entry.get_content(web_view.getContext()).toString(),  "text/html", "utf-8", null);

        }

        // Handles clicks to the closed button on the encyclopedia page
        close_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                page_stack.clear();
                anchors_stack.clear();
                closePage();
            }
        });
    }

    // make back button return to previous encyclopedia page
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            page_stack.pop();
            anchors_stack.pop();
            closePage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
