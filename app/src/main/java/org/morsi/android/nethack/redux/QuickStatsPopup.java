package org.morsi.android.nethack.redux;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.morsi.android.nethack.redux.util.QuickStat;
import org.morsi.android.nethack.redux.util.QuickStatCategory;

import java.util.ArrayList;

public class QuickStatsPopup {
    QuickStatsActivity activity;
    QuickStatCategory category;
    QuickStat stat;

    private ViewGroup popupView(){
        return (ViewGroup) activity.findViewById(R.id.quick_stat_page_popup);
    }

    private ArrayList<String> labels(){
        return category.get_columns();
    }

    View layout;

    private TextView pageTitle(){
        return (TextView) layout.findViewById(R.id.quick_stat_page_title);
    }

    private TextView pageContent(){
        return (TextView) layout.findViewById(R.id.quick_stat_page_content);
    }

    private Button closeButton(){
       return  (Button) layout.findViewById(R.id.quick_stat_page_close);
    }

    private View popupLayout(){
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.quick_stat_popup, popupView());
        return layout;
    }

    private PopupWindow popupWindow(){
        return new PopupWindow(popupLayout(), 230, 400, true);
    }

    public QuickStatsPopup(QuickStatsActivity activity, QuickStatCategory category, QuickStat stat){
        this.activity = activity;
        this.category = category;
        this.stat     = stat;

        setTitle();
        setContent();

        PopupWindow window = popupWindow();
        window.showAtLocation(layout, Gravity.CENTER, 0, 0);
        closeButton().setOnClickListener(new QuickStatPopupCloseListener(window));
    }

    private void setTitle(){
        pageTitle().setText(category.name);
    }

    private void setContent(){
        ArrayList<String> labels = labels();
        String content = "";
        for (int i = 0; i < labels.size(); ++i)
            content += labels.get(i) + ": " + stat.get_value(i) + "\n";
        pageContent().setText(content);
    }

    // Handles clicks to the closed button on the popup page
    class QuickStatPopupCloseListener implements Button.OnClickListener {
        private PopupWindow popup_window;

        public QuickStatPopupCloseListener(PopupWindow lpw) {
            popup_window = lpw;
        }

        public void onClick(View v) {
            popup_window.dismiss();
        }
    }
}
