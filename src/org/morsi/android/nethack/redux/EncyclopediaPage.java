
package org.morsi.android.nethack.redux;

import java.util.Stack;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    public static Stack<String> page_stack = new Stack<String>();

    // show a new topic via the encyclopedia page
    public static void showPage(Context context, String topic){
        if(page_stack.isEmpty() || !page_stack.peek().equals(topic))
            page_stack.push(topic);
        Intent EncPage = new Intent(context, EncyclopediaPage.class);
        EncPage.putExtra("page", topic);
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
        String current_page = getIntent().getExtras().getString("page");

        setContentView(R.layout.encyclopedia_popup);
        Button close_button = (Button) findViewById(R.id.encyclopedia_page_close);
        EncyclopediaEntry entry = EncyclopediaActivity.encyclopedia.get(current_page);

        TextView title = (TextView) findViewById(R.id.encyclopedia_page_title);
        WebView web_view = (WebView) findViewById(R.id.encyclopedia_page_content);

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
	                if(url.substring(0, 22).equals("fake://morsi.org/wiki/")){
	                    // TODO right now we are just dropping anchors, handle these properly at some point
	                    int end = url.indexOf('#');
	                    if(end == -1) end = url.length();

	                    String next_page = url.substring(22, end);
	                    page_stack.push(next_page);
	                    closePage();
	                    return true;
	                }
	                return false;
	           }
	        });

	        // need to use loadDataWithBaseURL
	        // http://code.google.com/p/android-rss/issues/detail?id=15
	        web_view.loadDataWithBaseURL("fake://morsi.org", entry.get_content(web_view.getContext()).toString(),  "text/html", "utf-8", null);
        }

        // Handles clicks to the closed button on the encyclopedia page
        close_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                page_stack.clear();
                closePage();
            }
        });
    }

    // make back button return to previous encyclopedia page
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            page_stack.pop();
            closePage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
