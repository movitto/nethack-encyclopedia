/**********************************
 * Nethack Encyclopedia - Tutorial Activity
 * 
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack;

import android.os.Bundle;

// Nethack Beginners Tutorial (interactive?)
public class TutorialActivity extends BaseNethackActivity {
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.tutorial);
	   }
}