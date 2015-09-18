package com.zzy.root;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import android.view.MenuItem;

import com.lidroid.xutils.ViewUtils;
import com.youth.zzy.cloudmemo.R;

/**
 * Created by djgzhiyong on 15/9/3.
 */
public abstract class RootAction extends ActionBarActivity {

    public Toolbar mToolbar;
    public ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewId());
        ViewUtils.inject(this);
        setActionBar();
        create();
    }


    private void setActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mActionBar = getSupportActionBar();
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public abstract int setContentViewId();

    public abstract void create();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(setMenuId(), menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return false;
        }
        menuClick(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public int setMenuId() {
        return R.menu.menu_empty;
    }

    public void menuClick(int id) {

    }
}
