package com.qin.gao.followme;

import android.text.Spannable;
import android.text.SpannableStringBuilder;

/**
 * Created by qq on 2017/9/21.
 */

public class Utils {
	public static class Direction {
		public static final int _U = 0;
		public static final int _D = 1;
		public static final int _L = 3;
		public static final int _R = 4;
	}
	public static int getDirection(float x1,float y1, float x2,float y2){
		x1 -=x2; y1 -=y2; x2= x1>0?x1:-x1; y2 = y1>0?y1:-y1;
		if (x2>=y2){
			if ( x1>=0 ) return Direction._L;
			return  Direction._R;
		}else {
			if( y1>=0 ) return Direction._U;
			return Direction._D;
		}
	}
	public static String ms2MMSS(int ms){
		ms /=1000;
		return String.format("%02d:%02d",ms/60,ms%60);
	}
	public static String ms2MMSS(int ms1,int ms2){
		return String.format("[%s/%s]",ms2MMSS(ms1),ms2MMSS(ms2));
	}
	public static String padingText( String s ){
		String ret="";
		for (int i=0;i<s.length();++i){
			if(s.charAt(i) == ' ') ret += s.charAt(i);
			else ret += '*';
		}
		return  ret;
	}
	public static SpannableStringBuilder toSSB( String s1, String s2,boolean show) {
		SpannableStringBuilder	ssb = new SpannableStringBuilder(s1 + "\n" + (show?s2:padingText(s2)));//用于可变字符串
		ssb.setSpan(Cfg.pre, 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(Cfg.cur, s1.length(), s1.length() + s2.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ssb;
	}

	public interface IFlippingMove{
		void moveU();
		void moveD();
		void moveL();
		void moveR();
	}

}
