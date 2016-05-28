/**********************************
 * Nethack Encyclopedia - Nethack Encyclopedia Activity
 * <p/>
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack.redux;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.AndroidMenu;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

// Main NethackEncyclopedia entry point activity,
//   simply provide means which to access sub-activities
public class NethackEncyclopedia extends Activity {
    // Override menu / about_dialog dialog handlers
    @Override
    public boolean onSearchRequested() {
        return AndroidMenu.onSearchRequested(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return AndroidMenu.onCreateOptionsMenu(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return AndroidMenu.onOptionsItemSelected(this, item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return AndroidMenu.onCreateDialog(this, id);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // loading the encyclopedia might take a while due to a lot of
        // content so launch the worker thread immediately
        EncyclopediaActivity.load_encyclopedia(this);
    }

    public void onClickQuickStatsButton(View target) {
        animateSelection(findViewById(R.id.quickstats_button),
                new Intent(NethackEncyclopedia.this, QuickStatsActivity.class));
    }

    public void onClickEncyclopediaButton(View target) {
        animateSelection(findViewById(R.id.encyclopedia_button),
                new Intent(NethackEncyclopedia.this, EncyclopediaActivity.class));
    }

    public void onClickMessagesButton(View target) {
        animateSelection(findViewById(R.id.messages_button),
                new Intent(NethackEncyclopedia.this, MessagesActivity.class));
    }

    public void onClickCalculatorButton(View target) {
        animateSelection(findViewById(R.id.calculator_button),
                new Intent(NethackEncyclopedia.this, CalculatorActivity.class));
    }

    public void onClickGameTrackerButton(View target) {
        animateSelection(findViewById(R.id.game_tracker_button),
                new Intent(NethackEncyclopedia.this, GameTrackerActivity.class));
    }

    public void onClickTutorialButton(View target) {
        animateSelection(findViewById(R.id.tutorial_button),
                new Intent(NethackEncyclopedia.this, TutorialActivity.class));
    }

    public void onClickAboutButton(View target) {
        showDialog(AndroidMenu.DIALOG_ABOUT_ID);
    }

    private void animateSelection(View view, final Intent intent){
        Animation anim = new ScaleAnimation(1.5f, 1.5f,                         // x start / end
                                            1f, 1f,                             // y start / end
                                            Animation.RELATIVE_TO_SELF, 0,      // x pivot
                                            Animation.RELATIVE_TO_SELF, 1f);    // y pivot
        anim.setDuration(250);
        anim.setFillAfter(false); // discard result of the animation

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation anim){
                NethackEncyclopedia.this.startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation anim) {}

            @Override
            public void onAnimationStart(Animation anim) {}
        });

        view.startAnimation(anim);
    }
}
