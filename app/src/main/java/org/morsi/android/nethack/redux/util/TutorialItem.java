package org.morsi.android.nethack.redux.util;

// encapsulates an item appearing in the tutorial
public class TutorialItem {
	public int group_id;
    public String content = "";
    public String image;
    
    public TutorialItem(int rgroup_id){
    	group_id = rgroup_id;
    	content = null; image = null;
    }
}
