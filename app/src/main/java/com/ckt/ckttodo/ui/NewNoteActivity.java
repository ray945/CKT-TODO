package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ckt.ckttodo.R;

public class NewNoteActivity extends AppCompatActivity {

    private ImageView iv_noteBack;
    private ImageView iv_noteSure;
    private EditText et_noteTitle;
    private EditText et_noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        init();
    }

    private void init() {
        initData();
        findView();
        setListener();
    }

    private void initData() {
        
    }

    private void setListener() {
        iv_noteBack.setOnClickListener(new MyOnclickListener());
        iv_noteSure.setOnClickListener(new MyOnclickListener());
    }

    private void findView() {
        iv_noteBack = (ImageView) findViewById(R.id.iv_noteBack);
        iv_noteSure = (ImageView) findViewById(R.id.iv_noteSure);
        et_noteTitle = (EditText) findViewById(R.id.et_noteTitle);
        et_noteContent = (EditText) findViewById(R.id.et_noteContent);
    }

    private class MyOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_noteBack:
                    finish();
                    break;
                case R.id.iv_noteSure:
                    saveNote();
                    break;
            }
        }
    }

    private void saveNote() {
        
    }
}
