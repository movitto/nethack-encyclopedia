package org.morsi.android.nethack.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import android.content.res.AssetManager;

public class Android {
	// helper to read the specified Android asset file into a string
    public static String assetToString(AssetManager manager, String asset){	
    	try{
	    	Writer writer = new StringWriter();
    		InputStream in = manager.open(asset);
    		Reader reader = new BufferedReader(new InputStreamReader(in));
    		int n; char[] buffer = new char[1024];
    		while ((n = reader.read(buffer)) != -1) {
    		  writer.write(buffer, 0, n);
    		}
    		return writer.toString();
    	} catch (IOException e) {
    		return "";
    	}
    }
}
