package com.ckt.ckttodo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ckt.ckttodo.R;

public class NoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, new NoteFragment()).commit();
    }
}