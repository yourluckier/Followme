package com.qin.gao.followme;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by qq on 2017/9/26.
 */

public class MyActionModeCallback implements ActionMode.Callback {
	protected Menu mMenu;
	protected Binding[] bindings;
	protected String text;

	@Override
	public boolean onCreateActionMode(ActionMode actionMode, final Menu menu) {
		menu.clear();

		actionMode.getMenuInflater().inflate(R.menu.textselected, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
		this.mMenu = menu;
		return true;
	}

	@Override
	public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode actionMode) {
	}
}
