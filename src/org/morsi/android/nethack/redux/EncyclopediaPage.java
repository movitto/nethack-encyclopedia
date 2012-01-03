
package org.morsi.android.nethack.redux;

import android.app.Activity;
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
	                    NextActivity(next_page);
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
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    //create previous page
    public void PreviousActivity() {
        int CurrentDepth = Integer.parseInt(getIntent().getExtras().getString("depth"));

        if(CurrentDepth > 0){
            Intent EncPage = new Intent(EncyclopediaPage.this, EncyclopediaPage.class);
            String PrevPage = getIntent().getExtras().getString("0");
            EncPage.putExtra("page", PrevPage );

            for (int i=1; i<CurrentDepth; i++){
                String aPage = getIntent().getExtras().getString(Integer.toString(i));
                EncPage.putExtra(Integer.toString(i-1),aPage);
            }

            CurrentDepth--;
            EncPage.putExtra("depth",Integer.toString(CurrentDepth));

            startActivity(EncPage);
            setResult(RESULT_OK);
            finish();
        }else{	//close the window if there aren't any more pages to go to
            setResult(RESULT_OK);
            finish();
        }
    }

    //create clicked page
    public void NextActivity(String NextPage) {
        int CurrentDepth = Integer.parseInt(getIntent().getExtras().getString("depth"));
        String current_page = getIntent().getExtras().getString("page");

        Intent EncPage = new Intent(EncyclopediaPage.this, EncyclopediaPage.class);
        EncPage.putExtra("page", NextPage);		//set the page for the next window

        // import the page history from previous intent
        for (int i=0; i<=CurrentDepth; i++){
            String aPage = getIntent().getExtras().getString(Integer.toString(i));
            EncPage.putExtra(Integer.toString(i+1), aPage);
        }

        // push current page onto the top of the history stack
        EncPage.putExtra("0", current_page);
        CurrentDepth++;
        EncPage.putExtra("depth",Integer.toString(CurrentDepth));

        startActivity(EncPage);
        setResult(RESULT_OK);
        finish();
    }

    // make back button return to previous encyclopedia page
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            PreviousActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
