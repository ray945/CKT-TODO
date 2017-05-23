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
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HTTPConstants;
import com.ckt.ckttodo.network.HTTPHelper;
import com.ckt.ckttodo.network.HTTPService;
import com.ckt.ckttodo.util.NetworkService;
import com.ckt.ckttodo.util.OptimizeInteractonUtils;

import okhttp3.Request;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by zhiwei.li on 2017/3/17.
 */

public class LoginActivity extends AppCompatActivity implements BaseView {

    private static final String TAG = "LoginActivity";
    private EditText et_account;
    private EditText et_password;
    private Button loginBtn;

    private static final int REQUEST_SIGNUP = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        loginBtn.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.landing));
        progressDialog.show();

        String emailText = et_account.getText().toString().trim();
        String passwordText = et_password.getText().toString().trim();

        // do network request
        Map< String, String > map = new HashMap<>();
        map.put(BeanConstant.USERNAME, emailText);
        map.put(BeanConstant.PASSWORD, passwordText);
        Request request = HTTPHelper.getGetRequest(map, HTTPConstants.PATH_LOGIN);
        HTTPService.getHTTPService().doHTTPRequest(request, this);


        // Authenticating
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override public void run() {
//                onLoginSuccess();
//                progressDialog.dismiss();
//
//                // network test
//                Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("http://10.120.3.191:8080/")
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .build();
//
//                NetworkService service = retrofit.create(NetworkService.class);
//                service.login("1","1").enqueue(new Callback<String>() {
//                    @Override public void onResponse(Call<String> call, Response<String> response) {
//                        Log.e("Network", response.body());
//                        if ("true".equals(response.body())){
//                            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//
//                    @Override public void onFailure(Call<String> call, Throwable t) {
//
//                    }
//                });
//            }
//        }, 3000);

    }


    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
        loginBtn.setEnabled(true);
    }


    private void onLoginSuccess() {
        loginBtn.setEnabled(true);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    private boolean validate() {
        boolean valid = true;

        String emailText = et_account.getText().toString().trim();
        String passwordText = et_password.getText().toString().trim();
//        if (emailText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
//            et_account.setError(getString(R.string.please_input_email));
//            valid = false;
//        } else {
//            et_account.setError(null);
//        }

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

    @Override
    public void replyRequestResult(String strJson) {

        Log.d(TAG, "replyRequestResult: " + strJson);
        Toast.makeText(this, "Successful!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void replyNetworkErr() {

        Toast.makeText(this, "Network Error!", Toast.LENGTH_SHORT).show();

    }
}
