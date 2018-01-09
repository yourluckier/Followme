package com.qin.gao.followme;

import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by qq on 2017/9/26.
 */

public  class Binding{
	Menu menu;
	MenuItem item;
	public int id;
	public MenuItem.OnMenuItemClickListener listener;
	public Binding ( int id_,MenuItem.OnMenuItemClickListener listener_){
		id = id_;
		listener = listener_;
	}
	public Binding bind(Menu menu){
		this.menu = menu;
		(item = menu.findItem(id)).setOnMenuItemClickListener(listener);
		return this;
	}
}
