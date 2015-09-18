package com.zzy.cloudmemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.youth.zzy.cloudmemo.R;
import com.zzy.root.MainApplication;
import com.zzy.root.RootAction;

import cn.bmob.v3.Bmob;

public class MainActivity extends RootAction {


    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawerLayout;

    @ViewInject(R.id.main_drawer)
    private FrameLayout frameLayout;

    private MainApplication app;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void create() {
        app = (MainApplication) getApplication();

        initDrawer();

        initBmob();
        setContent();
    }

    private void initDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_drawer));
    }

    private void setContent() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, new ContentFragment());

        DrawerFragment drawerFragment = new DrawerFragment();
        drawerFragment.setDrawerLayout(drawerLayout);
        transaction.replace(R.id.main_drawer, drawerFragment);
        transaction.commit();
    }


    private void initBmob() {
        Bmob.initialize(this, "7050ba80bf4d81e1e77eeb87a66cb0ec");
        app.syncData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            if (drawerLayout.isDrawerOpen(frameLayout)) {

                drawerLayout.closeDrawers();

            } else {

                drawerLayout.openDrawer(frameLayout);

            }
            return false;
        }
        return super.onOptionsItemSelected(item);
    }


    public static final int RESULT_ADD = 0x1;
}
