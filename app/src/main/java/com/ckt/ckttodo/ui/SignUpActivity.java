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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.util.NetworkService;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import com.ckt.ckttodo.database.UserInfo;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HTTPConstants;
import com.ckt.ckttodo.network.HTTPHelper;
import com.ckt.ckttodo.network.HTTPService;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by zhiwei.li on 2017/3/20.
 */

public class SignUpActivity extends AppCompatActivity implements BaseView {

    private EditText et_name;
    private EditText et_email;
    private EditText et_mobileNumber;
    private EditText et_password;
    private EditText et_reEnterPassword;
    private Button signUpBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }


    private void initView() {
        et_name = (EditText) findViewById(R.id.input_name);
        et_email = (EditText) findViewById(R.id.input_email);
        et_mobileNumber = (EditText) findViewById(R.id.input_mobile);
        et_password = (EditText) findViewById(R.id.input_password);
        et_reEnterPassword = (EditText) findViewById(R.id.input_reEnterPassword);
        signUpBtn = (Button) findViewById(R.id.btn_signup);
        TextView loginLink = (TextView) findViewById(R.id.link_login);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

    }


    private void signUp() {
        if (!validate()) {
            onSignUpFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.registering));
        progressDialog.show();

        String nameText = et_name.getText().toString().trim();
        String emailText = et_email.getText().toString().trim();
        String mobileNumberText = et_mobileNumber.getText().toString().trim();
        String passwordText = et_password.getText().toString().trim();
        String reEnterPasswordText = et_reEnterPassword.getText().toString().trim();

        UserInfo info = new UserInfo();
        info.setMem_name(nameText);
        info.setMem_email(emailText);
        info.setMem_phone_num(mobileNumberText);
        info.setMem_password(passwordText);
        JSONObject object = new JSONObject();
        object.put(BeanConstant.USER, info);
        Map<String, String> map = new HashMap<>();
        map.put(BeanConstant.DATA, object.toJSONString());
        Request request = HTTPHelper.getPostRequest(map, HTTPConstants.PATH_SIGNUP);
        HTTPService.getHTTPService().doHTTPRequest(request, this);

        // Authenticating
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override public void run() {
//                onSignUpSuccess();
//                progressDialog.dismiss();
//
//                Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("http://10.120.3.191:8080/")
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .build();
//
//                NetworkService service = retrofit.create(NetworkService.class);
//                service.register("test_name","test_pwd","test@email.com","6","6").enqueue(new Callback<String>() {
//                    @Override public void onResponse(Call<String> call, Response<String> response) {
//                        Log.e("Network",response.body());
//                        if ("true".equals(response.body())){
//                            Toast.makeText(SignUpActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//
//                    @Override public void onFailure(Call<String> call, Throwable t) {
//
//                    }
//                });
//
//            }
//        },3000);
    }


    @Override
    public void replyRequestResult(String strJson) {
        JSONObject json = JSON.parseObject(strJson);
        switch (json.getInteger(BeanConstant.RESULT_CODE)) {

            case BeanConstant.SUCCESS_RESULT_CODE:
                Toast.makeText(this, getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BeanConstant.SIGNUP_EMAIL_EXSIT_RESULT_CODE:
                Toast.makeText(this, getString(R.string.signup_email_exist), Toast.LENGTH_SHORT).show();
                break;
            case BeanConstant.SIGNUP_PASS_DATA_ERRO_RESULT_CODE:
                //TODO 系统错误

                 break;

        }
    }

    @Override
    public void replyNetworkErr() {
        Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private void onSignUpFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.signup_failed), Toast.LENGTH_SHORT)
                .show();
        signUpBtn.setEnabled(true);
    }

    private void onSignUpSuccess() {
        signUpBtn.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
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


    private boolean validate() {
        boolean valid = true;
        String nameText = et_name.getText().toString().trim();
        String emailText = et_email.getText().toString().trim();
        String mobileNumberText = et_mobileNumber.getText().toString().trim();
        String passwordText = et_password.getText().toString().trim();
        String reEnterPasswordText = et_reEnterPassword.getText().toString().trim();

        if (nameText.isEmpty() || nameText.length() < 2) {
            et_name.setError(getString(R.string.at_least_3_characters));
            valid = false;
        } else {
            et_name.setError(null);
        }

        if (emailText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            et_email.setError(getString(R.string.please_input_email));
            valid = false;
        } else {
            et_email.setError(null);
        }

        if (mobileNumberText.isEmpty() || mobileNumberText.length() != 11) {
            et_mobileNumber.setError(getString(R.string.enter_valid_mobile_number));
            valid = false;
        } else {
            et_mobileNumber.setError(null);
        }

        if (passwordText.isEmpty()) {
            et_password.setError(getString(R.string.please_input_password));
            valid = false;
        } else {
            et_password.setError(null);
        }

        if (reEnterPasswordText.isEmpty() || !reEnterPasswordText.equals(passwordText)) {
            et_reEnterPassword.setError(getString(R.string.password_do_not_match));
        }

        return valid;
    }
}
