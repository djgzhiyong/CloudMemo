package com.zzy.cloudmemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordListener;
import cn.bmob.v3.listener.SaveListener;

import com.youth.zzy.cloudmemo.R;
import com.zzy.root.RootAction;
import com.zzy.utils.BaseSave;
import com.zzy.utils.Helper;
import com.zzy.utils.NetWorkState;
import com.zzy.widget.MyToast;

public class LoginActivity extends RootAction {
    private EditText etEmail, etPwd;
    private Button btnLogin, btnRegister;
    private TextView txtPwdForget;
    private ProgressDialog proDialog;
    private MyToast toast;

    @Override
    public int setContentViewId() {
        return R.layout.activity_cloud_login;
    }

    @Override
    public void create() {
        setTitle("登录账户");
        toast = new MyToast(this);
        txtPwdForget = (TextView) findViewById(R.id.login_pwdForget);
        etEmail = (EditText) findViewById(R.id.login_etUserName);
        etPwd = (EditText) findViewById(R.id.login_etPwd);
        btnLogin = (Button) findViewById(R.id.login_btnLogin);
        btnRegister = (Button) findViewById(R.id.login_btnRegister);
        txtPwdForget.setOnClickListener(btnClick);
        btnLogin.setOnClickListener(btnClick);
        btnRegister.setOnClickListener(btnClick);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 5 && etPwd.getText().length() > 5) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
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
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 5 && etEmail.getText().length() > 5) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
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


    OnClickListener btnClick = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btnLogin:
                    if (NetWorkState.getNetWorkState(LoginActivity.this) != NetWorkState.NETWORK_DISCONNECT) {
                        userLogin(etEmail.getText().toString(), etPwd.getText()
                                .toString());
                    } else {
                        toast.show("当前无网络连接");
                    }
                    break;
                case R.id.login_btnRegister:
                    toRegisterActivity();
                    break;
                case R.id.login_pwdForget:
                    showPwdForgetDialog();
                    break;
            }
        }
    };

    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, RESULT_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_REGISTER && data != null) {
            String email = data.getStringExtra("email");
            etEmail.setText(email);
            etPwd.setText("");
            etPwd.requestFocus();
        }
    }

    private void userLogin(final String userName, String password) {
        showProDialog();
        BmobUser user = new BmobUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.login(this, new SaveListener() {

            public void onSuccess() {
                proDialog.dismiss();
                toast.show("登录成功");
                BaseSave.saveUserName(LoginActivity.this, userName);
                setResult(CloudFragment.RESULT_LOGIN);
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                proDialog.dismiss();
                msg = "邮箱或密码有误";
                toast.show(msg);
            }
        });
    }

    private void showPwdForgetDialog() {
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("输入注册时的邮箱来重置密码");
        View view = getLayoutInflater().inflate(R.layout.dialog_pwd_forget,
                null);
        final EditText et = (EditText) view
                .findViewById(R.id.pwdForget_etInput);
        ad.setView(view);
        ad.setPositiveButton("重置密码", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {
                final String email = et.getText().toString();
                if (!Helper.checkEmail(email)) {
                    toast.show("邮箱格式有误");
                    return;
                }
                if (NetWorkState.getNetWorkState(LoginActivity.this) != NetWorkState.NETWORK_DISCONNECT) {
                    showProDialog();
                    BmobUser.resetPassword(LoginActivity.this, email,
                            new ResetPasswordListener() {
                                public void onSuccess() {
                                    proDialog.dismiss();
                                    toast.show("密码重置申请成功，需登录到" + email + "完成操作");
                                }

                                @Override
                                public void onFailure(int state, String str) {
                                    proDialog.dismiss();
                                    str = "当前邮箱不存在";
                                    toast.show(str);
                                }
                            });
                } else {
                    toast.show("当前无网络连接");
                }
            }
        });
        ad.create().show();
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

    public static final int RESULT_REGISTER = 0x1;
}
