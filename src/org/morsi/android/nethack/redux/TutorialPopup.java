
package org.morsi.android.nethack.redux;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import org.morsi.android.nethack.redux.R;

// Show encyclopedia page screen when encyclopedia topic is clicked
public class TutorialPopup extends Activity
{
	
	public static void showPopup(Context context, String image){
        Intent EncPage = new Intent(context, TutorialPopup.class);
        EncPage.putExtra("image", image);
        context.startActivity(EncPage);
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_popup);

        String image_res = "tutorial_" + getIntent().getExtras().getString("image");
        ImageView img = (ImageView)findViewById(R.id.tutorial_popup_image);
        Resources res = getResources();
        int resID = res.getIdentifier(image_res, "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID);
        img.setImageDrawable(drawable);

        // Handles clicks to the closed button on the encyclopedia page
        Button close_button = (Button) findViewById(R.id.tutorial_close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
