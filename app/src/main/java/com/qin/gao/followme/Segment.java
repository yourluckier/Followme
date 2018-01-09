package com.qin.gao.followme;

/**
 * Created by qq on 2017/9/14.
 */

public class Segment {
	public Segment(String line){
		int min = 10 * (line.charAt(1) - '0') + (line.charAt(2) - '0');
		int sec = ( 10000 * (line.charAt(4) - '0') + 1000*(line.charAt(5) - '0') + 100* (line.charAt(7) - '0') + 10*(line.charAt(8) - '0'));
		head = min * 60000 + sec;
		text = line.substring(10);
	}
	public int head;
	public int tail;
	public String text;
	public boolean inTheSegment(int pos){
		return pos>=head && pos <= tail;
	}
}
