package com.ckt.ckttodo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.ResultUser;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HttpClient;
import com.ckt.ckttodo.retrofit.UserService;
import com.ckt.ckttodo.util.OptimizeInteractonUtils;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by zhiwei.li on 2017/3/17.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "mozre";
    private EditText et_account;
    private EditText et_password;
    private Button loginBtn;
    private ProgressDialog mProgressDialog;

    private static final int REQUEST_SIGNUP = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        loginStatus();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        TextView signUpLink = (TextView) findViewById(R.id.link_sign_up);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptimizeInteractonUtils.hideKeyboard(view.getWindowToken(), view.getContext());
                login();
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                //          finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void loginStatus() {
        User user = new User(this);
        if (user.getmIsLogin()) {
            onLoginSuccess();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }


    private void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

//        loginBtn.setEnabled(false);
        mProgressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getString(R.string.landing));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        String emailText = et_account.getText().toString().trim();
        String passwordText = et_password.getText().toString().trim();

        UserService userService = HttpClient.getHttpService(UserService.class);
        userService.doLogin(emailText, passwordText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResultUser value) {
                        switch (value.getResultcode()) {

                            case BeanConstant.SUCCESS_RESULT_CODE:

                                User user = new User(LoginActivity.this, value.getData());
                                user.setmToken(value.getToken());
                                mProgressDialog.dismiss();
                                onLoginSuccess();
                                break;

                            case BeanConstant.LOGIN_FAIL_RESULT_CODE:
                                mProgressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    private void onLoginFailed() {
//        Toast.makeText(getBaseContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
        loginBtn.setEnabled(true);
    }


    private void onLoginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    private boolean validate() {
        boolean valid = true;

        String emailText = et_account.getText().toString().trim();
        String passwordText = et_password.getText().toString().trim();
        if (emailText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            et_account.setError(getString(R.string.please_input_email));
            valid = false;
        } else {
            et_account.setError(null);
        }

        if (passwordText.isEmpty()) {
            et_password.setError(getString(R.string.please_input_password));
            valid = false;
        } else {
            et_password.setError(null);
        }

        return valid;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
