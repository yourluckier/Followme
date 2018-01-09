package com.qin.gao.followme;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.style.ForegroundColorSpan;

import java.util.Random;

public class Cfg {
	public static class WX{
		public static final String APP_ID="wx49d8426f7b66d162";
		public static final String APP_SEC="b0f71058413f4dab22c1629ae1f2c1e1";
	}
	private static final String[] alipayments = {
			"FKX02285ZKJVJZ5RNRUFA6", //wdd
			"FKX098985YJC6MUWLGDPA8",//jlb
			"FKX02285ZKJVJZ5RNRUFA6",//wdd
			"FKX03848QBOHWN9OL2Z939",//chengqian
	};
	public static final String Title = "www.follow-me.vip";

	public static final ForegroundColorSpan pre = new ForegroundColorSpan(Color.GRAY);
	public static final ForegroundColorSpan cur = new ForegroundColorSpan(Color.GREEN);

	public static final String MsgPause = "Paused";
	public static final String Announcement = "\t本软件所用的音频资源来自互联网,仅用于学习交流之用.";
	public static final String AnnouncementII = "播放完毕!如果你觉得我们的软件不错就打赏点吧,么么哒!";
    public static final String AnnouncementIII = "木有装支付宝?";
//	public static final String Web = "http://www.follow-me.vip";
	public static final String Web = "http://47.95.207.149";
	public static final String baidu = "http://fanyi.baidu.com/#en/zh";
	public static final String download="https://pan.baidu.com/s/1kVuwirH#list/path=%2FVoa";

	public static final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Web));
	public static final int interval = 200;

	public static final Random r = new Random();
	public static final String getLuckyOne() {
		return alipayments[r.nextInt(alipayments.length)];
	}
	public static final int[] bg = new int[]{R.drawable.bground};

	public static final int[] darkness = new int[]{0, 4, 8, 12,14};

}
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//		 new MenuInflater(this).inflate(R.menu.textselected, menu);
////		ui.setupMenuItems(menu);
//	}