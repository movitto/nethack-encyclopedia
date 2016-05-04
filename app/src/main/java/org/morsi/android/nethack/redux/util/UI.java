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
    public static void showView(Activity activity, int view_id){
        View v = activity.findViewById(view_id);
        v.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = RelativeLayout.LayoutParams.FILL_PARENT;
        v.setLayoutParams(lp);
    }

    public static void hideView(Activity activity, int view_id){
        View v = activity.findViewById(view_id);
        v.setVisibility(View.INVISIBLE);
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = 0;
        v.setLayoutParams(params);
    }
}
