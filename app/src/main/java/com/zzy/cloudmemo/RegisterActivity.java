package com.zzy.cloudmemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.youth.zzy.cloudmemo.R;
import com.zzy.root.RootAction;
import com.zzy.utils.Helper;
import com.zzy.utils.NetWorkState;
import com.zzy.widget.MyToast;

public class RegisterActivity extends RootAction {
    private EditText etPwd, etEmail;
    private Button btnOk;
    private ProgressDialog proDialog;
    private MyToast toast;


    @Override
    public int setContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    public void create() {
        setTitle("注册账户");
        etPwd = (EditText) findViewById(R.id.register_etPwd);
        etEmail = (EditText) findViewById(R.id.register_etEmail);
        btnOk = (Button) findViewById(R.id.register_btnregister);
        btnOk.setOnClickListener(registerClick);
        toast = new MyToast(this);

        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 5 && etEmail.getText().length() > 3) {
                    btnOk.setEnabled(true);
                } else {
                    btnOk.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 5 && etPwd.getText().length() > 5) {
                    btnOk.setEnabled(true);
                } else {
                    btnOk.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    OnClickListener registerClick = new OnClickListener() {
        public void onClick(View v) {
            if (NetWorkState.getNetWorkState(RegisterActivity.this) != NetWorkState.NETWORK_DISCONNECT) {

                BmobUser bmob = new BmobUser();
                String password = etPwd.getText().toString();
                final String email = etEmail.getText().toString();

                if (!Helper.checkEmail(email)) {
                    toast.show("邮箱格式有误");
                    return;
                }

                showProDialog();
                bmob.setUsername(email);
                bmob.setPassword(password);
                bmob.setEmail(email);
                bmob.signUp(RegisterActivity.this, new SaveListener() {
                    public void onSuccess() {
                        proDialog.dismiss();
                        toast.show("注册成功");
                        registerDone(email);
                    }

                    public void onFailure(int state, String str) {
                        proDialog.dismiss();
                        str = "注册失败，当前邮箱已被注册";
                        toast.show(str);
                    }
                });
            } else {
                toast.show("当前无网络连接");
            }
        }
    };

    private void registerDone(String email) {
        Intent intent = new Intent();
        intent.putExtra("email", email);
        setResult(LoginActivity.RESULT_REGISTER, intent);
        finish();
    }

    private void showProDialog() {
        proDialog = new ProgressDialog(this);
        proDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
