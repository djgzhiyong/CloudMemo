package com.zzy.cloudmemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.youth.zzy.cloudmemo.R;
import com.zzy.adapter.LeftAdapter;
import com.zzy.bean.LeftItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djgzhiyong on 15/9/13.
 */
public class DrawerFragment extends Fragment {

    @ViewInject(R.id.drawer_listview)
    private ListView mListView;

    private DrawerLayout drawerLayout;

    private static final String items[] = {"记录", "云同步", "关于"};

    private int mCurrentSelectedPosition = 0;

    private FragmentManager mFragmentManager;

    private Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        ViewUtils.inject(this, view);
        setDrawerData();
        return view;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    private void setDrawerData() {
        List<LeftItemBean> data = new ArrayList<LeftItemBean>();
        data.add(new LeftItemBean(R.drawable.ic_edit, items[0]));
        data.add(new LeftItemBean(R.drawable.ic_cloud, items[1]));
        data.add(new LeftItemBean(R.drawable.ic_about, items[2]));
        mListView.setAdapter(new LeftAdapter(getActivity(), data));
        mListView.setItemChecked(mCurrentSelectedPosition, true);
    }


    @OnItemClick(R.id.drawer_listview)
    public void itemClick(AdapterView<?> view, View v, int position, long id) {
        getActivity().setTitle(items[position]);
        changeFragment(position);
//        mDrawerLayout.closeDrawers();
    }

    private void changeFragment(int position) {
        switch (position) {
            case 0:
                fragment = new ContentFragment();
                break;
            case 1:
                fragment = new CloudFragment();
                break;
            case 2:
                fragment = new AboutFragment();
                break;
        }
        mFragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();

        drawerLayout.closeDrawers();
    }


}
