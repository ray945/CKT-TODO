package com.ckt.ckttodo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Note;
import com.ckt.ckttodo.databinding.ActivityNewNoteBinding;

public class NewNoteActivity extends AppCompatActivity {

    private ImageView iv_noteBack;
    private ImageView iv_noteSure;
    private EditText et_noteTitle;
    private EditText et_noteContent;
    private String mNoteTag;
    private Note noteGet;
    private ActivityNewNoteBinding mActivityNewNoteBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityNewNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_note);
        init();
    }

    private void init() {
        findView();
        initData();
        setListener();
    }

    private void initData() {
        Intent intent = getIntent();
        mNoteTag = intent.getStringExtra("noteTag");
        if (mNoteTag.equals("1")) {
            Bundle bundle = intent.getBundleExtra("noteGet");
            noteGet = (Note) bundle.getSerializable("noteGet");
            et_noteTitle.setText(noteGet.getNoteTitle());
            et_noteContent.setText(noteGet.getNoteContent());
        } else {

        }
    }

    private void setListener() {
        iv_noteBack.setOnClickListener(new MyOnclickListener());
        iv_noteSure.setOnClickListener(new MyOnclickListener());
    }

    private void findView() {
        iv_noteBack = mActivityNewNoteBinding.ivNoteBack;
        iv_noteSure = mActivityNewNoteBinding.ivNoteSure;
        et_noteTitle = mActivityNewNoteBinding.etNoteTitle;
        et_noteContent = mActivityNewNoteBinding.etNoteContent;
    }

    private class MyOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_noteBack:
                    finish();
                    break;
                case R.id.iv_noteSure:
                    if (et_noteContent.getText().toString() == null || et_noteTitle.getText().toString() == null) {
                        Toast.makeText(NewNoteActivity.this, "不能为空哦!", Toast.LENGTH_SHORT).show();
                    } else if (mNoteTag.equals("1")) {
                        updateNote();
                    } else {
                        saveNote();
                    }
                    break;
            }
        }
    }

    private void updateNote() {
        noteGet.setNoteContent(et_noteContent.getText().toString());
        noteGet.setNoteTitle(et_noteTitle.getText().toString());
        DatebaseHelper.getInstance(this).update(noteGet);
    }

    private void saveNote() {
        Note note = new Note();
        note.setNoteContent(et_noteContent.getText().toString());
        note.setNoteTitle(et_noteTitle.getText().toString());
        DatebaseHelper.getInstance(this).insert(note);
    }
}
