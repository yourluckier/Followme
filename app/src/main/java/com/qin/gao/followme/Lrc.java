package com.qin.gao.followme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by qq on 2017/9/14.
 */

public class Lrc{
	protected Vector<Segment> segments;
	public Lrc(InputStream is,int end) {
		segments = new Vector<Segment>();
		BufferedReader reader = new BufferedReader( new InputStreamReader(is));
		String str = null;
		boolean bStarted = false;
		while (true) {
			try {
				str = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(str!=null) {
				System.out.println(str);
				if (str.startsWith("[00:0") ) bStarted = true;
				char x = str.charAt(0);
				if(bStarted) segments.add( new Segment(str) );
			}else {
				break;
			}
		}
		for(int i=0;i<segments.size()-1;++i)
			segments.elementAt(i).tail = segments.elementAt(i+1).head;
		segments.elementAt(0).head = 0;
		segments.elementAt(segments.size()-1).tail = end;
	}
	public String getText(int i){
		if (i<0 || i>segments.size()-1)
			return "";
		return segments.elementAt(i).text;
	}
}
