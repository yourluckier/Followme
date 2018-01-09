package com.qin.gao.followme;

public class Status {
	public int currentSeg = 0;
	public boolean repeated = !true;
	public boolean showText = true;
	public boolean paused = false;
	public int firstSeg = 0;
	public int lastSeg = 1;
	public Status() {}
	public Status( int lastSeg ){
		this.lastSeg = lastSeg;
	}
	public boolean canBckward(){
		return currentSeg>firstSeg;
	}
	public boolean canForward(){
		return currentSeg<lastSeg;
	}
	public void reverseShowText(){
		showText = !showText;
	}
	public void reverseRepeat(){
		repeated= !repeated;
	}
	public String getStatus() {
		return String.format("[%02d/%02d][%s循环][%s中...]"
				,currentSeg+1,
				lastSeg+1,
				!repeated?"全文":"分段",
				paused?"暂停":"播放");
	}
	public void Jump( int steps ){
		currentSeg += steps;
		if(currentSeg>lastSeg) currentSeg = lastSeg;
		else  if(currentSeg<firstSeg) currentSeg = firstSeg;
	}
	public void onEnding(){
		if(!repeated)
			currentSeg = firstSeg;
	}
	public void Fwd(){
		if(currentSeg==lastSeg) currentSeg=0;
		else currentSeg++;
	}
	public String getPlayMod(){
		return repeated?"切换到分段循环模式":"切换到全文循环模式";
	}
}
