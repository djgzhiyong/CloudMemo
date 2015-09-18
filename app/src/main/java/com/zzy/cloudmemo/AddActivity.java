package com.zzy.cloudmemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.youth.zzy.cloudmemo.R;
import com.zzy.adapter.ColorsAdapter;
import com.zzy.bean.ColorSelect;
import com.zzy.dao.MemoDaoHelper;
import com.zzy.model.MemoBean;
import com.zzy.root.Constance;
import com.zzy.root.RootAction;
import com.zzy.widget.MyToast;

public class AddActivity extends RootAction {

    @ViewInject(R.id.add_color)
    private CardView colorView;

    private EditText etInput;
    private MyToast mToast;
    private MemoDaoHelper memoDao;
    private String oldContent;
    private int id, position;
    private Dialog dialog;
    private LayoutInflater layoutIn;
    private List<ColorSelect> colorList;
    private ColorsAdapter colorAdapter;
    private String bgColor = "#FFFFFF";


    @Override
    public int setContentViewId() {
        return R.layout.activity_add;
    }

    @Override
    public void create() {
        etInput = (EditText) findViewById(R.id.add_edit);
        etInput.setSingleLine(false);
        mToast = new MyToast(this);
        memoDao = new MemoDaoHelper(this);
        layoutIn = LayoutInflater.from(this);
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        position = intent.getIntExtra("position", -1);
        if (position > -1) {
            setTitle("查看&编辑");
            oldContent = intent.getStringExtra("content");
            bgColor = intent.getStringExtra("color");
            etInput.setText(oldContent);
            etInput.setSelection(oldContent.length());
            colorView.setCardBackgroundColor(Color.parseColor(bgColor));
//            mToolbar.setBackgroundColor(Color.parseColor(bgColor));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuIn = getMenuInflater();
        menuIn.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu_save:
                saveMemo();
                break;
            case R.id.add_menu_share:
                String text = etInput.getText().toString();
                if (text.trim().length() > 0) {
                    share(text);
                } else {
                    mToast.show("输入内容后方可分享");
                }
                break;
            case R.id.add_menu_color:
                showColorDialog();
                break;
            case android.R.id.home:
                String content = etInput.getText().toString();
                if (content.trim().length() > 0) {
                    saveMemo();
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveMemo() {
        String content = etInput.getText().toString();
        if (position == -1) {
            addMemo(content);
        } else {
            updateMemo(content);
        }
    }

    /**
     * 添加内容
     */
    private void addMemo(String content) {
        if (content.trim().length() == 0) {
            mToast.show("请输入内容");
        } else {
            long addTime = System.currentTimeMillis();
            memoDao.save(new MemoBean(content, addTime, bgColor));
            setResult(ContentFragment.RESULT_ADD, getIntent(content, addTime));
            finish();
        }
    }

    private Intent getIntent(String content, long addTime) {
        Intent intent = new Intent();
        intent.putExtra("content", content);
        intent.putExtra("addTime", addTime);
        intent.putExtra("position", position);
        intent.putExtra("color", bgColor);
        return intent;
    }

    /**
     * 更新内容
     */
    private void updateMemo(String content) {
        memoDao.updateMemo(id, content, bgColor);
        setResult(ContentFragment.RESULT_EDIT, getIntent(content, 0));
        finish();
    }

    private void share(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void showColorDialog() {
        View view = layoutIn.inflate(R.layout.dialog_colors, null);
        GridView gridview = (GridView) view.findViewById(R.id.colors_gridview);
        if (colorList == null) {
            colorList = new ArrayList<ColorSelect>();
            colorList.add(new ColorSelect(0, false));
            colorList.add(new ColorSelect(1, false));
            colorList.add(new ColorSelect(2, false));
            colorList.add(new ColorSelect(3, false));
            colorList.add(new ColorSelect(4, false));
            colorList.add(new ColorSelect(5, false));
            colorList.add(new ColorSelect(6, false));
            colorList.add(new ColorSelect(7, true));
        }
        setSelectColor();
        colorAdapter = new ColorsAdapter(this, colorList);
        gridview.setAdapter(colorAdapter);
        gridview.setOnItemClickListener(colorSelectClick);
        dialog = new Dialog(this);
        dialog.setTitle("颜色");
        dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        dialog.show();
    }

    private void setSelectColor() {
        int select = 7;
        for (int i = 0; i < Constance.COLORS.length; i++) {
            if (bgColor.equals(Constance.COLORS[i])) {
                select = i;
                break;
            }
        }
        for (int i = 0; i < colorList.size(); i++) {
            if (i == select) {
                colorList.get(i).isSelect = true;
            } else {
                colorList.get(i).isSelect = false;
            }
        }
    }

    OnItemClickListener colorSelectClick = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            dialog.dismiss();
            ColorSelect color = colorList.get(position);
            notifyData(color.colorId);
            bgColor = Constance.COLORS[position];
            colorView.setCardBackgroundColor(Color.parseColor(bgColor));
//            mToolbar.setBackgroundColor(Color.parseColor(bgColor));
        }
    };

    private void notifyData(int colorId) {
        for (ColorSelect cs : colorList) {
            if (cs.colorId == colorId) {
                cs.isSelect = true;
            } else {
                cs.isSelect = false;
            }
        }
        colorAdapter.notifyDataSetChanged();
    }

}
