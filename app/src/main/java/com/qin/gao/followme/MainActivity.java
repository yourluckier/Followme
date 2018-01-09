package com.qin.gao.followme;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneFavorite;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

abstract class MyUI{
	public TextView textView = null;
	public TextView label = null;
	public TextView masks = null;
	public ProgressBar prgBar = null;
	public Binding[] bindings = null;
	protected WindowManager.LayoutParams params;
	public MyUI() {
		params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.TOP;
		params.y = 10;// 距离底部的距离是10像素 如果是 top 就是距离top是10像素
	}
	public String getSelected(){
		if( textView==null ) return "";
		int h = textView.getSelectionStart();
		int t = textView.getSelectionEnd();
		return textView.getText().toString().substring(h,t);
	}
}

abstract class MyDlg{
	protected TextView tv;
	public   AlertDialog.Builder dlg;

	public MyDlg( Context ctx ){
		tv = new TextView(ctx);
		tv.setText(Cfg.MsgPause);
		tv.setPadding(10, 10, 10, 10);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(23);
		tv.setBackgroundColor(Color.argb(255, 0, 255, 0));
        tv.getBackground().setAlpha(127);
		dlg = new AlertDialog.Builder(ctx);
		dlg.setCustomTitle(tv).setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				OnDismiss();
			}
		});
	}
	public void Show( String msg ){
		tv .setText(msg);
        dlg.show();
	}
	abstract void OnDismiss();
}

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
	class UI extends MyUI {
		UI(int t) {
			super();
			(textView = (TextView) findViewById(R.id.textView)).setOnTouchListener(MainActivity.this);
			textView.setLongClickable(true);
			textView.setCursorVisible(true);
			textView.setTextIsSelectable(true);

			textView.setCustomSelectionActionModeCallback(new TheActionModeCallback());
			label = (TextView) findViewById(R.id.label);
			masks = new TextView(MainActivity.this);

			(prgBar = (ProgressBar) findViewById(R.id.progressBar)).setMax(t);

			((WindowManager) getSystemService(Context.WINDOW_SERVICE)).addView(masks, params);
		}

		public void update(boolean tv) {
			prgBar.setProgress(player.getCurrentPosition());
			if (tv)
				textView.setText(Utils.toSSB(preventText(), currentText(), status.showText), TextView.BufferType.SPANNABLE);
			label.setText(status.getStatus() + getTimeInfo());
		}

		class MenuItemClickListener_home_page implements MenuItem.OnMenuItemClickListener {

			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				startActivity(Cfg.intent);
				return false;
			}
		}

		class MenuItemClickListener_text_on implements MenuItem.OnMenuItemClickListener {

			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				status.reverseShowText();
				update(true);
				return false;
			}
		}

		class MenuItemClickListener_donate implements MenuItem.OnMenuItemClickListener {

			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				donateByAlipay();
				return false;
			}
		}

		class MenuItemClickListener_wx implements MenuItem.OnMenuItemClickListener {
//			public static final int WXSceneSession = 0; WXSceneTimeline = 1;WXSceneFavorite = 2;
            public int WXScene;
			public MenuItemClickListener_wx( int nType ){
				WXScene = nType;
			}
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				wsm.shareByWeixin(wsm.new ShareContentWebpage("follow.me","hello",Cfg.Web,R.drawable.homepage), WXScene);
				return false;
			}
		}

		class MenuItemClickListener_light implements MenuItem.OnMenuItemClickListener {
			public int light=0;

			public MenuItemClickListener_light ( int light){
				this.light = light;
			}
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				masks.setBackgroundColor(Cfg.darkness[light]<<28);
				return false;
			}
		}


		class MenuItemClickListener_share_qc implements MenuItem.OnMenuItemClickListener {

			private String url;

			public MenuItemClickListener_share_qc( String url ){
				this.url= url;
			}
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				ImageView iv = new ImageView(MainActivity.this);
				Bitmap qrBitmap = generateBitmap(Cfg.download,400, 400);
				iv.setImageBitmap(qrBitmap);
				new AlertDialog.Builder(MainActivity.this).setCustomTitle( iv ).show();
				return false;
			}
		}

		public void setupMenuItems(Menu menu) {
			bindings = new Binding[]{
					new Binding(R.id.home_page, new MenuItemClickListener_home_page()).bind(menu),
					new Binding(R.id.text_on, new MenuItemClickListener_text_on()).bind(menu),
					new Binding(R.id.donate, new MenuItemClickListener_donate()).bind(menu),
					new Binding(R.id.wx_sc, new MenuItemClickListener_wx( WXSceneFavorite )).bind(menu),
					new Binding(R.id.wx_pyq, new MenuItemClickListener_wx( WXSceneTimeline)).bind(menu),
					new Binding(R.id.wx_fs, new MenuItemClickListener_wx( WXSceneSession)).bind(menu),
					new Binding(R.id.light_a, new MenuItemClickListener_light( 0 )).bind(menu),
					new Binding(R.id.light_b, new MenuItemClickListener_light( 1 )).bind(menu),
					new Binding(R.id.light_c, new MenuItemClickListener_light( 2 )).bind(menu),
					new Binding(R.id.light_d, new MenuItemClickListener_light( 3 )).bind(menu),
					new Binding(R.id.share_qr, new MenuItemClickListener_share_qc( Cfg.Web )).bind(menu),
			};
		}
	}

	class TheActionModeCallback extends MyActionModeCallback {
		class MenuItemClickListener_badu_fy implements MenuItem.OnMenuItemClickListener {

			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Cfg.baidu + "/" + ui.getSelected())));
				mMenu.close();
				return false;
			}
		}

		class MenuItemClickListener_fy_sentence implements MenuItem.OnMenuItemClickListener {

			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Cfg.baidu + "/" + currentText())));
				mMenu.close();
				return false;
			}
		}

		@Override
		public boolean onCreateActionMode(ActionMode actionMode, final Menu menu) {
			super.onCreateActionMode(actionMode, menu);

			bindings = new Binding[]{
					new Binding(R.id.it_badu_fy, new MenuItemClickListener_badu_fy()).bind(menu),
					new Binding(R.id.it_fy_sentence, new MenuItemClickListener_fy_sentence()).bind(menu),
			};

			Snackbar.make(ui.textView, consultTheDictionary(), Snackbar.LENGTH_LONG).setDuration(5000)
					.setAction("+",
							new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									wsm.shareByWeixin( wsm.new ShareContentText(consultTheDictionary()), WXSceneFavorite );
								}
							}).show();

			return true;
		}
	}

	class TheOnGestureListener extends MyOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
			switch (Utils.getDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
				case Utils.Direction._U:
					return moveU();
				case Utils.Direction._D:
					return moveD();
				case Utils.Direction._L:
					return moveL();
				case Utils.Direction._R:
					return moveR();
			}
			return false;
		}
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
            ui.update(msg.what == 2 );
			super.handleMessage(msg);
		}
	}

	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			if (!player.isPlaying()) return;
			int curPos = player.getCurrentPosition();
			Segment s = currentSeg();
			if (s.inTheSegment(curPos)) {
				if (toTheEndOfCurSpan(curPos)) doSkip();
				else handler.sendEmptyMessage(1);
			} else if (status.repeated) {
				player.seekTo(s.head + 100);
			} else {
				while (!currentSeg().inTheSegment(curPos))
					status.currentSeg += curPos > s.tail ? 1 : -1;
				handler.sendEmptyMessage(1);
			}
		}
	}

	class MyPauseDlg extends MyDlg {
		public MyPauseDlg(Context ctx) {
			super(ctx);
		}

		@Override
		void OnDismiss() {
			if (status.paused)
				play();
		}
	}

	class MyOnCompletionListener implements  MediaPlayer.OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mediaPlayer) {
			Snackbar.make(ui.textView, Cfg.AnnouncementII, Snackbar.LENGTH_LONG)
					.setAction("赏",
							new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									donateByAlipay();
								}
							}).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

        wsm= WeixinShareManager.getInstance(MainActivity.this);

		(player = MediaPlayer.create(this, R.raw.nce3001)).setLooping(true);
		player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
			@Override
			public void onSeekComplete(MediaPlayer mediaPlayer) {
				ui.update(!status.repeated);
			}
		});
		player.setOnCompletionListener(new MyOnCompletionListener());

		ui = new UI(player.getDuration());

		scripts = new Lrc(getResources().openRawResource(R.raw.nce3001_), player.getDuration());
		dic = new MyDic(getResources().openRawResource(R.raw.dic));
		dic2 = new MyDic(getResources().openRawResource(R.raw.dic2));
		status = new Status(scripts.segments.size() - 1);
		gd = new GestureDetector(getApplicationContext(), new TheOnGestureListener());
		move(0, true);
		Toast.makeText(MainActivity.this, Cfg.Announcement, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_menu, menu);
		ui.setupMenuItems(menu);

//		menu.add(0, 1, Menu.NONE, "蓝牙发送").setIcon(android.R.drawable.ic_menu_send);
//
//		//添加子菜单
//		SubMenu subMenu = menu.addSubMenu(0,2,Menu.NONE, "重要程度>>").setIcon(android.R.drawable.ic_menu_share);
//		//添加子菜单项
//		subMenu.add(2, 201, 1, "☆☆☆☆☆");
//		subMenu.add(2, 202, 2, "☆☆☆");
//		subMenu.add(2, 203, 3, "☆");
//
//		menu.add(0, 3, Menu.NONE, "重命名").setIcon(android.R.drawable.ic_menu_edit);
//		menu.add(0, 4, Menu.NONE, "删除").setIcon(android.R.drawable.ic_menu_close_clear_cancel);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onStop() {
		super.onStop();
		pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		play();
	}

	@Override
	public boolean onTouch(View view, MotionEvent me) {
		return gd.onTouchEvent(me);
	}

	private boolean move(int d, boolean skip) {
		status.Jump(d);
		if (skip) player.seekTo(currentSeg().head + 10);
		ui.update(d != 0);
		return false;
	}

	private boolean moveR() {
		return move(status.canForward() ? +1 : -status.lastSeg, true);
	}

	private boolean moveL() {
		return move(status.canBckward() ? -1 : +status.lastSeg, true);
	}

	private boolean moveU() {
		if (!status.paused) {
			pause();
			new MyPauseDlg(MainActivity.this).Show(Cfg.MsgPause);
		}
		return false;
	}

	private boolean moveD() {
		status.reverseRepeat();
		ui.update(false);
		Toast.makeText(getApplicationContext(), status.getPlayMod(), Toast.LENGTH_SHORT).show();
		return false;
	}

	private void pause() {
		status.paused = true;
		timer.cancel();
		if (player.isPlaying())
			player.pause();
		ui.update(false);
	}

	private void play() {
		status.paused = false;
		(timer = new Timer()).schedule(new MyTimerTask(), 0, Cfg.interval);
		if (!player.isPlaying())
			player.start();
		ui.update(false);
	}

	private Segment currentSeg() {
		return scripts.segments.elementAt(status.currentSeg);
	}

	private String currentText() {
		return currentSeg().text;
	}

	private String preventText() {
		return status.currentSeg == 0 ? " " : scripts.segments.elementAt(status.currentSeg - 1).text;
	}

	private String getTimeInfo() {
		return Utils.ms2MMSS(player.getCurrentPosition(), player.getDuration());
	}

	private void donateByAlipay() {
		if (AliPay.hasInstalledAlipayClient(MainActivity.this))
			AliPay.startAlipayClient(MainActivity.this, Cfg.getLuckyOne());
		else
			Snackbar.make(ui.textView, Cfg.AnnouncementIII, Snackbar.LENGTH_LONG).setDuration(2000).show();
	}

	private void doSkip() {
		if (!status.repeated)
			status.Fwd();
		player.seekTo(currentSeg().head + 10);
	}

	private boolean toTheEndOfCurSpan(int curPos) {
		return currentSeg().tail - curPos < Cfg.interval;
	}

	private String consultTheDictionary() {
		String selected = ui.getSelected();
		String answer1 = dic2.find(selected);
		String answer2 = dic.find(selected);
		String result = selected + ": ";

		if (answer1 != null) result += answer1;
		if (answer2 != null) result += " " + answer2;
		return result;
	}
	private Bitmap generateBitmap(String content,int width, int height) {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		Map<EncodeHintType, String> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
			BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (encode.get(j, i)) {
						pixels[i * width + j] = 0x00000000;
					} else {
						pixels[i * width + j] = 0xffffffff;
					}
				}
			}
			return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}
	private UI ui;
	private MediaPlayer player;
	private Lrc scripts;
	private MyDic dic, dic2;
	private Timer timer;
	private Handler handler = new MyHandler();
	private GestureDetector gd;
	private Status status;

	private WeixinShareManager wsm ;
}
