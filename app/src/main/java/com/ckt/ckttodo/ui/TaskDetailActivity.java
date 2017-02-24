package com.ckt.ckttodo.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.databinding.ActivityTaskDetailBinding;
import com.ckt.ckttodo.util.Constants;

import org.w3c.dom.Text;

public class TaskDetailActivity extends AppCompatActivity {

    public final static String EVENT_TASK_ID = "event_task";
    private ActivityTaskDetailBinding mActivityTaskDetailBinding;
    private TextView mTextViewKinds;
    private TextView mTextViewLevels;
    private TextView mTextViewRemind;
    private EditText mEditTextSpendTime;
    private EventTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.task_detail));
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        mActivityTaskDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!TextUtils.isEmpty(mEditTextSpendTime.getText())) {
                String content = mEditTextSpendTime.getText().toString().replace(" ","");
                if (content.length() > 0) {
                    float update = Float.valueOf(content);

                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mTextViewKinds = mActivityTaskDetailBinding.detailTextKinds;
        mTextViewLevels = mActivityTaskDetailBinding.detailTextLevel;
        mTextViewRemind = mActivityTaskDetailBinding.detailTextRemind;
        mEditTextSpendTime = mActivityTaskDetailBinding.detailEditSendTime;
        setData();
    }

    private void setData() {
        String taskID = getIntent().getStringExtra(EVENT_TASK_ID);
        if (taskID != null) {
            mTask = DatebaseHelper.getInstance(this).getRealm().where(EventTask.class).contains(EventTask.TASK_ID, taskID).findFirst();
            mActivityTaskDetailBinding.setTask(mTask);
            mActivityTaskDetailBinding.executePendingBindings();
            mTextViewKinds.setText(transKinds(mTask.getTaskType()));
            mTextViewLevels.setText(transLevel(mTask.getTaskPriority()));
            mTextViewRemind.setText(transRemind(mTask.getTaskRemindTime()));
        }
    }

    private String transLevel(int i) {
        String[] levels = getResources().getStringArray(R.array.task_level);
        return levels[i - 1];
    }

    private String transKinds(int i) {
        String[] kinds = getResources().getStringArray(R.array.kind_list);
        return kinds[i - 1];
    }

    private String transRemind(long i) {
        String[] reminds = getResources().getStringArray(R.array.task_remind);
        if (i == Constants.TEN_MIN_TO_SEC) {
            return reminds[0];
        } else if (i == Constants.TWENTY_MIN_TO_SEC) {
            return reminds[1];
        } else if (i == Constants.HALF_HOUR_TO_SEC) {
            return reminds[2];
        } else if (i == Constants.ONE_HOUR_TO_SEC) {
            return reminds[3];
        } else {
            return "";
        }
    }

}
