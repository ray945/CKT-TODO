package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ckt.ckttodo.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_about);
    }

    public void onClick() {
        finish();
    }
}
