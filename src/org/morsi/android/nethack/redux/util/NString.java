package org.morsi.android.nethack.redux.util;

// A wrapper around an array of non-unicode bytes to
// get around inconsistencies w/ string handling between
// parser.rb and the Android app
public class NString {
  private byte[] content;
  
  public NString(String s){  
	  content = s.getBytes();
  }
  
  public NString(byte[] b){
	  content = b;
  }
  
  public int length(){
	  return content.length;
  }
  
  public byte byteAt(int i){
	  return content[i];
  }
  
  public NString substring(int start_index, int end_index){
	  int length = end_index-start_index;
	  byte[] new_nstring = new byte[length];
	  for(int i = start_index; i < end_index; ++i)
		  new_nstring[i-start_index] = content[i];
	  return new NString(new_nstring);
  }
  
  public String toString(){
	  return new String(content);
  }
  
  public int indexOf(byte search, int start_at){
	  for(int i = start_at; i < content.length; ++i)
		  if(content[i] == search)
			  return i;
	  return -1;
	  
  }
}
