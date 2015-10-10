package com.zzy.cloudmemo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.melnykov.fab.FloatingActionButton;
import com.youth.zzy.cloudmemo.R;
import com.zzy.adapter.ContentGridAdapter;
import com.zzy.dao.MemoDaoHelper;
import com.zzy.model.MemoBean;
import com.zzy.utils.AnimListenr;
import com.zzy.widget.MyToast;

import java.util.List;

public class ContentFragment extends Fragment {
    @ViewInject(R.id.content_add)
    private FloatingActionButton floatButtonAdd;



    private MyToast mToast;
    private GridView gridView;
    private TextView txtHint;
    private Button btnDel, btnCancel;
    private ContentGridAdapter gridAdapter;
    private MemoDaoHelper memoDao;
    private List<MemoBean> memoList;
    private LinearLayout footerLayout;
    private Animation footerShowAnim, footerExitAnim, downAnim, upAnim;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memoDao = new MemoDaoHelper(getActivity());
        mToast = new MyToast(getActivity());
        footerShowAnim = AnimationUtils.loadAnimation(getActivity(),
                R.anim.footer_show);
        footerExitAnim = AnimationUtils.loadAnimation(getActivity(),
                R.anim.footer_exit);
        downAnim = AnimationUtils
                .loadAnimation(getActivity(), R.anim.down_anim);
        upAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.up_anim);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, null);
        ViewUtils.inject(this, view);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        gridView = (GridView) view.findViewById(R.id.content_gridview);
        gridView.setOnItemClickListener(listItemClick);
        txtHint = (TextView) view.findViewById(R.id.content_txtHint);
        footerLayout = (LinearLayout) view.findViewById(R.id.content_bottom);
        btnDel = (Button) view.findViewById(R.id.content_btnDel);
        btnCancel = (Button) view.findViewById(R.id.content_btnCancel);
        btnDel.setOnClickListener(btnClick);
        btnCancel.setOnClickListener(btnClick);
        gridView.setOnScrollListener(mScrollListener);
    }


    private void initData() {
        memoList = memoDao.findAll();
        if (memoList != null && memoList.size() > 0) {
            showGridStyle();
            txtHint.setVisibility(View.GONE);
        } else {
            txtHint.setVisibility(View.VISIBLE);
        }
    }

    private void showGridStyle() {
        gridAdapter = new ContentGridAdapter(getActivity(), memoList);
        gridView.setAdapter(gridAdapter);
        floatButtonAdd.attachToListView(gridView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_content, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.content_menu_delete:
                if (memoList != null && memoList.size() > 0) {

                    if (gridAdapter.getIsDeleteShow()) {
                        if (getIsCanDel()) {
                            showDeleteDialog();
                        } else {
                            mToast.show("请选择删除项");
                        }
                    } else {
                        gridAdapter.setDeleteShow(true);
                        footerLayout.setVisibility(View.VISIBLE);
                        footerLayout.startAnimation(footerShowAnim);
                        hideAddLayout();
                    }
                } else {
                    mToast.show("当前无数据");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toAddActivity(int position) {
        Intent intent = new Intent(getActivity(), AddActivity.class);
        if (position > -1) {
            MemoBean memo = memoList.get(position);
            intent.putExtra("content", memo.content);
            intent.putExtra("id", memo.id);
            intent.putExtra("position", position);
            intent.putExtra("color", memo.color);
            startActivityForResult(intent, RESULT_EDIT);
        } else {
            startActivityForResult(intent, RESULT_ADD);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_ADD) {
            initData();
            // String content = data.getStringExtra("content");
            // long addTime = data.getLongExtra("addTime",
            // System.currentTimeMillis());
            // String color = data.getStringExtra("color");
            // int position = data.getIntExtra("position", -1);
            // MemoBean memo = new MemoBean(content, addTime, color);
            // dataChange(position, memo);
            //
            // if (styleState == Constance.STYLE_LIST) {
            // if (listAdapter == null) {
            // showListStyle();
            // } else {
            // listAdapter.notifyDataSetChanged();
            // }
            // } else {
            // if (gridAdapter == null) {
            // showGridStyle();
            // } else {
            // gridAdapter.notifyDataSetChanged();
            // }
            // }
            // if (txtHint.getVisibility() == View.VISIBLE) {
            // txtHint.setVisibility(View.GONE);
            // }
        } else if (resultCode == RESULT_EDIT) {
            initData();
            // int position = data.getIntExtra("position", -1);
            // MemoBean memo = memoList.get(position);
            // memo.content = data.getStringExtra("content");
            // memo.color = data.getStringExtra("color");
            // if (styleState == Constance.STYLE_LIST) {
            // listAdapter.notifyDataSetChanged();
            // } else {
            // gridAdapter.notifyDataSetChanged();
            // }
        }
    }

    // private void dataChange(int position, MemoBean memo) {
    // if (memoList == null) {
    // memoList = new ArrayList<MemoBean>();
    // }
    // if (position == -1) {
    // memoList.add(0, memo);
    // } else {
    // memoList.remove(position);
    // memoList.add(position, memo);
    // }
    // }

    private void showDeleteDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        ad.setMessage("确认删除当前选择项?");
        ad.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int size = memoList.size();
                for (int i = size - 1; i >= 0; i--) {
                    MemoBean memo = memoList.get(i);
                    if (memo.isDel == MemoBean.DELETE) {
                        memoDao.deleteMemo(memo.id);
                        memoList.remove(i);
                    }
                }
                deleteDone();
                mToast.show("删除完成");
                showAddLayout();
            }
        });
        ad.setNegativeButton("取消", null);
        ad.create().show();
    }

    private boolean getIsCanDel() {
        for (int i = 0; i < memoList.size(); i++) {
            MemoBean memo = memoList.get(i);
            if (memo.isDel == MemoBean.DELETE) {
                return true;
            }
        }
        return false;
    }

    OnItemClickListener listItemClick = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            MemoBean memo = memoList.get(position);

            if (gridAdapter.getIsDeleteShow()) {
                memo.isDel = memo.isDel == MemoBean.RETAIN ? MemoBean.DELETE
                        : MemoBean.RETAIN;
                gridAdapter.notifyDataSetChanged();
                btnDel.setText("删除(" + getDelCount() + ")");
            } else {
                toAddActivity(position);
            }

        }
    };

    OnClickListener btnClick = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.content_btnDel:
                    if (getIsCanDel()) {
                        showDeleteDialog();
                    } else {
                        mToast.show("请选择删除项");
                    }
                    break;
                case R.id.content_btnCancel:
                    gridAdapter.setDeleteShow(false);
                    clearDataDelState();
                    footerLayout.startAnimation(footerExitAnim);
                    footerLayout.setVisibility(View.GONE);

                    showAddLayout();
                    break;
            }
        }
    };

    @OnClick(R.id.content_add)
    public void addClick(View v) {
        toAddActivity(-1);
    }

    private void clearDataDelState() {
        int size = memoList.size();
        for (int i = 0; i < size; i++) {
            memoList.get(i).isDel = MemoBean.RETAIN;
        }
    }

    /**
     * 获取删除数据数量
     */
    private int getDelCount() {
        int count = 0;
        int size = memoList.size();
        for (int i = 0; i < size; i++) {
            if (memoList.get(i).isDel == MemoBean.DELETE) {
                count++;
            }
        }
        return count;
    }

    /**
     * 删除完成
     */
    private void deleteDone() {
        if (memoList.size() == 0) {
            txtHint.setVisibility(View.VISIBLE);
        }
        showGridStyle();
        footerLayout.startAnimation(footerExitAnim);
        footerLayout.setVisibility(View.GONE);
    }

    OnScrollListener mScrollListener = new OnScrollListener() {

        int firstVisibleItem, visibleItemCount, totalItemCount;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (visibleItemCount == totalItemCount) {
                return;
            }
            if (scrollState == SCROLL_STATE_IDLE && !gridAdapter.getIsDeleteShow()) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (floatButtonAdd.getVisibility() == View.VISIBLE) {
                        hideAddLayout();
                    }
                } else {
                    if (floatButtonAdd.getVisibility() == View.GONE) {
                        showAddLayout();
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            this.firstVisibleItem = firstVisibleItem;
            this.visibleItemCount = visibleItemCount;
            this.totalItemCount = totalItemCount;
        }
    };

    /**
     * 隐藏添加按钮
     */
    private void hideAddLayout() {
        floatButtonAdd.startAnimation(downAnim);
        downAnim.setAnimationListener(new AnimListenr(floatButtonAdd));
    }

    /**
     * 显示添加按钮
     */
    private void showAddLayout() {
        floatButtonAdd.startAnimation(upAnim);
        upAnim.setAnimationListener(new AnimListenr(floatButtonAdd));
    }


    public static final int RESULT_ADD = 0x1;

    public static final int RESULT_EDIT = 0x2;
}
