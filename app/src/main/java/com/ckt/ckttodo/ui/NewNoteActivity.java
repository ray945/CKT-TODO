package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Transition;
import android.view.KeyEvent;
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

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by mozre
 */

public class NewNoteActivity extends AppCompatActivity {

    private EditText et_noteTitle;
    private EditText et_noteContent;
    private String mNoteTag;
    private ActivityNewNoteBinding mActivityNewNoteBinding;
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
        if (Build.VERSION.SDK_INT >= 21){
            setupWindowAnimations();
        }
    }

    private void setupWindowAnimations() {
        Transition transition;
        transition = buildEnterTransition();
        getWindow().setEnterTransition(transition);
    }

    private Transition buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }

    private void init() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) NewNoteActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mActivityNewNoteBinding.etNoteTitle, 0);
            }
        }, 200);
        findView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sure:
                save();
                break;
            case android.R.id.home:
                if (mNoteTag.equals("1")) {
                    if (note.getNoteContent().equals(et_noteContent.getText().toString().trim()) && note.getNoteTitle().equals(et_noteTitle.getText().toString().trim())) {
                        onBackPressed();
                    } else {
                        show();
                    }
                } else {
                    show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        if (et_noteContent.getText().toString().trim().equals("") && et_noteTitle.getText().toString().trim().equals("")) {
            onBackPressed();
        } else if (mNoteTag.equals("1")) {
            updateNote();
        } else {
            saveNote();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mNoteTag.equals("1")) {
                if (note.getNoteContent().equals(et_noteContent.getText().toString().trim()) && note.getNoteTitle().equals(et_noteTitle.getText().toString().trim())) {
                } else {
                    show();
                }
            } else {
                show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void show() {
        if ((et_noteTitle.getText().toString().trim().equals("") && et_noteContent.getText().toString().trim().equals(""))) {
            onBackPressed();
        } else {
            new AlertDialog.Builder(this).setTitle("是否保存？").setPositiveButton("是", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    save();
                }
            }).setNegativeButton("否", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            }).show();
        }
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
        onBackPressed();
    }

    private void saveNote() {
        Note note = new Note();
        note.setNoteContent(et_noteContent.getText().toString().trim());
        note.setNoteTitle(et_noteTitle.getText().toString().trim());
        note.setNoteId(UUID.randomUUID().toString());
        DatebaseHelper.getInstance(this).insert(note);
        mIntent.putExtra("new", "1");
        setResult(1, mIntent);
        Toast.makeText(this, "新建成功", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
