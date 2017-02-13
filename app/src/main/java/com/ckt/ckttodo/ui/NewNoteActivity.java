package com.ckt.ckttodo.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Note;
import com.ckt.ckttodo.databinding.ActivityNewNoteBinding;

import io.realm.Realm;
import io.realm.RealmResults;

public class NewNoteActivity extends AppCompatActivity {

    private EditText et_noteTitle;
    private EditText et_noteContent;
    private String mNoteTag;
    private ActivityNewNoteBinding mActivityNewNoteBinding;
    private Realm mRealm;
    private Intent mIntent;
    private RealmResults<Note> baseList;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.new_note));
        mActivityNewNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_note);
        init();
    }

    private void init() {
        findView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_space:
                
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sure:
                mRealm = DatebaseHelper.getInstance(NewNoteActivity.this).getRealm();
                if (et_noteContent.getText().toString().trim().equals("") || et_noteTitle.getText().toString().trim().equals("")) {
                    Toast.makeText(NewNoteActivity.this, "不能为空哦!", Toast.LENGTH_SHORT).show();
                } else if (mNoteTag.equals("1")) {
                    updateNote();
                } else {
                    saveNote();
                }
                break;
            case android.R.id.home:
                finish();
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        baseList = DatebaseHelper.getInstance(this).findAll(Note.class);
        mIntent = getIntent();
        mNoteTag = mIntent.getStringExtra("noteTag");
        if (mNoteTag.equals("1")) {
            getSupportActionBar().setTitle(getResources().getString(R.string.alter_note));
            int mPosition = mIntent.getIntExtra("noteGet", 0);
            note = baseList.get(mPosition);
            et_noteTitle.setText(note.getNoteTitle());
            et_noteContent.setText(note.getNoteContent());
        } else {

        }
        mActivityNewNoteBinding.llSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_noteContent.setFocusable(true);
                et_noteContent.setFocusableInTouchMode(true);
                et_noteContent.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_noteContent,0);
            }
        });
        
    }


    private void findView() {
        et_noteTitle = mActivityNewNoteBinding.etNoteTitle;
        et_noteContent = mActivityNewNoteBinding.etNoteContent;
    }


    private void updateNote() {
        DatebaseHelper.getInstance(this).getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                note.setNoteContent(et_noteContent.getText().toString().trim());
                note.setNoteTitle(et_noteTitle.getText().toString().trim());
                realm.copyToRealmOrUpdate(note);
            }
        });
        mIntent.putExtra("update", "0");
        setResult(0, mIntent);
        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveNote() {
        Note note = new Note();
        note.setNoteContent(et_noteContent.getText().toString().trim());
        note.setNoteTitle(et_noteTitle.getText().toString().trim());
        int id;
        if (mRealm.where(Note.class).count() == 0) {
            id = 0;
        } else {
            RealmResults<Note> plans = mRealm.where(Note.class).findAllSorted("noteId", false);
            id = plans.first().getNoteId();
            id += 1;
        }
        RealmResults<Note> notes = mRealm.where(Note.class).findAllSorted("noteId", false);
        note.setNoteId(id);
        DatebaseHelper.getInstance(this).insert(note);
        mIntent.putExtra("new", "1");
        setResult(1, mIntent);
        Toast.makeText(this, "新建成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
