package com.ckt.ckttodo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ckt.ckttodo.R;
import org.w3c.dom.Text;

/**
 * Created by zhiwei.li on 2017/3/20.
 */

public class SignUpActivity extends AppCompatActivity {

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
            @Override public void onClick(View view) {
                signUp();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
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

        signUpBtn.setEnabled(false);
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

        // Authenticating
        new android.os.Handler().postDelayed(new Runnable() {
            @Override public void run() {
                onSignUpSuccess();
                progressDialog.dismiss();
            }
        },3000);
    }


    private void onSignUpFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.signup_failed), Toast.LENGTH_SHORT)
            .show();
        signUpBtn.setEnabled(true);
    }

    private void onSignUpSuccess(){
        signUpBtn.setEnabled(true);
        setResult(RESULT_OK,null);
        finish();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
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

        if (nameText.isEmpty() || nameText.length() < 3) {
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
