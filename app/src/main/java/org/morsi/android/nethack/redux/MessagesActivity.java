package org.morsi.android.nethack.redux;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.morsi.android.nethack.redux.util.GameMessage;

import java.util.ArrayList;

public class MessagesActivity extends Activity {

    // Actual messages to search
    ArrayList<GameMessage> messages;

    EditText searchView(){ return (EditText)findViewById(R.id.search_messages);}
    LinearLayout resultsView(){ return (LinearLayout)findViewById(R.id.search_messages_results); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messages = GameMessage.getMessagesFromXml(this);
        setContentView(R.layout.messages);

        searchView().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runSearch(s.toString());
            }
        });
    }

    protected void runSearch(String search){
        Log.d(getClass().getName(), "Searching for: " + search);
        resultsView().removeAllViews();
        if(search.length() == 0) return;

        for(GameMessage msg : messages) {
            if (msg.game.contains(search))
                resultsView().addView(resultView(msg));
        }
    }

    private TextView resultView(GameMessage msg){
        TextView result = new TextView(this);
        result.setText(msg.meaning);
        result.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return result;
    }
}
