package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.databinding.ActivityMainBinding;
import com.ckt.ckttodo.databinding.ActivityNewTaskBinding;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner mSpinnerTaskKinds;
    private Spinner mSpinnerTaskLevel;
    private ArrayAdapter<String> mSpinnerTaskKindsAdapter;
    private ArrayAdapter<String> mSpinnerTaskLevelAdapter;
    private String[] mArrayTaskKinds;
    private String[] mArrayTaskLevel;
    private ActivityNewTaskBinding mActivityNewTaskBinding;
    public static final String GET_PLAN_ID_FROM_PROJECT = "planId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.new_task));
//        setContentView(R.layout.activity_new_task);
        mActivityNewTaskBinding = DataBindingUtil.setContentView(NewTaskActivity.this,R.layout.activity_new_task);
        init();
//        initTimePicker();
    }

    private void init() {

        // 任务分类的下拉栏
        mSpinnerTaskKinds = mActivityNewTaskBinding.spinnerTaskChoose;
//        mSpinnerTaskKinds = (Spinner) findViewById(R.id.spinner_task_choose);
        mArrayTaskKinds = getResources().getStringArray(R.array.kind_list);
        mSpinnerTaskKindsAdapter = new ArrayAdapter<>(this, R.layout.common_text_item, mArrayTaskKinds);
        mSpinnerTaskKindsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTaskKinds.setAdapter(mSpinnerTaskKindsAdapter);
        //任务等级的下拉栏

        mSpinnerTaskLevel = mActivityNewTaskBinding.spinnerTaskLevel;
//        mSpinnerTaskLevel = (Spinner) findViewById(R.id.spinner_task_level);
        mArrayTaskLevel = getResources().getStringArray(R.array.task_level);
        mSpinnerTaskLevelAdapter = new ArrayAdapter<>(this, R.layout.common_text_item, mArrayTaskLevel);
        mSpinnerTaskLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTaskLevel.setAdapter(mSpinnerTaskLevelAdapter);
    }

    private void initTimePicker() {
        TimePickerDialog dialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        }, 0, 0, true);
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sure:

                break;
            case R.id.menu_research:

                break;
            case android.R.id.home:
                finish();
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
