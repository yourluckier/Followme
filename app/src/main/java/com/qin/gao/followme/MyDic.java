package com.qin.gao.followme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MyDic {
	HashMap<String, String> map = new HashMap<String, String>();

	public MyDic(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String str = null;
		while (true) {
			try {
				str = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (str != null) {
				String[] word = str.split(":");
				if (word.length == 2) {
					map.put(word[0].toLowerCase(), word[1]);
				}
			} else {
				break;
			}
		}
	}

	public String find(String s) {
		s = s.toLowerCase();
		String ret = map.get(s);
		while(ret==null && s.length()>0)
			ret = map.get( s = s.substring(0,s.length()-1) );
		return ret;
	}
}
