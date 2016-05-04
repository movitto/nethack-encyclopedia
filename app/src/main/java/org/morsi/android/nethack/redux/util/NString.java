package org.morsi.android.nethack.redux.util;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

// A wrapper around an array of non-unicode bytes to
// get around inconsistencies w/ string handling between
// parser.rb and the Android app
public class NString {
    private byte[] content;

    public static NString fromAsset(AssetManager manager, String asset) {
        try {
            Writer writer = new StringWriter();
            InputStream in = manager.open(asset);
            Reader reader = new BufferedReader(new InputStreamReader(in));
            int n;
            char[] buffer = new char[1024];
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            NString str = new NString(writer.toString());
            return str;
        } catch (IOException e) {
            return new NString("");
        }
    }

    public NString(String s) {
        content = s.getBytes();
    }

    public NString(byte[] b) {
        content = b;
    }

    public int length() {
        return content.length;
    }

    public byte byteAt(int i) {
        return content[i];
    }

    public NString substring(int start_index, int end_index) {
        int length = end_index - start_index;
        byte[] new_nstring = new byte[length];
        for (int i = start_index; i < end_index; ++i)
            new_nstring[i - start_index] = content[i];
        return new NString(new_nstring);
    }

    public String toString() {
        return new String(content);
    }

    public int indexOf(byte search, int start_at) {
        for (int i = start_at; i < content.length; ++i)
            if (content[i] == search)
                return i;
        return -1;

    }
}
