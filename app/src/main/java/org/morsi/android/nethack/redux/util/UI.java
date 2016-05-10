package org.morsi.android.nethack.redux.util;
;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by mmorsi on 4/30/16.
 */
public class UI {
    public static void showView(View v){
        v.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        v.setLayoutParams(lp);
    }

    public static void hideView(View v){
        v.setVisibility(View.INVISIBLE);
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = 0;
        v.setLayoutParams(params);
    }
}
