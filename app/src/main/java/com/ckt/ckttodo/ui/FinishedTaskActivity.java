package com.ckt.ckttodo.ui;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatabaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.databinding.ActivityFinishedTaskBinding;
import com.ckt.ckttodo.databinding.TaskListItemBinding;
import com.ckt.ckttodo.widgt.TaskDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

/**
 *  created by mozre
 */

public class FinishedTaskActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MenuItem mMenuItemSure;
    private MenuItem mMenuItemCancel;
    private ActionBar mActionBar;
    private List<EventTask> mFinishedTasks = new ArrayList<>();
    private ActivityFinishedTaskBinding mActivityFinishedTaskBinding;
    private FinishedTaskRecyclerAdapter mAdapter;
    private boolean isShowCheckBox = false;
    private Map<Integer, Boolean> mItemSelectedStatus = new HashMap<>();
    private DatabaseHelper mHelper = DatabaseHelper.getInstance(this);
    private Transition transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            setupWindowAnimations();
        }
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getResources().getString(R.string.task_history));
        mActivityFinishedTaskBinding = DataBindingUtil.setContentView(this, R.layout.activity_finished_task);
        init();
    }

    private void setupWindowAnimations() {
        transition = buildEnterTransition();
        getWindow().setEnterTransition(transition);
    }

    private Visibility buildEnterTransition() {
        Slide enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.setSlideEdge(Gravity.RIGHT);
        return enterTransition;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenuItemSure = menu.findItem(R.id.menu_sure);
        mMenuItemCancel = menu.findItem(R.id.menu_delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                mMenuItemSure.setVisible(false);
                mMenuItemCancel.setVisible(false);
//                showMenuItem(false);
                deleteSelectDataAndNotifyDataChanged();
                break;
            case R.id.menu_sure:
//                mMenuItemSure.setVisible(false);
//                mMenuItemCancel.setVisible(false);
//                deleteSelectDataAndNotifyDataChanged();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }


        return true;
    }

    /**
     * delete selected data in database
     */
    private void deleteSelectDataAndNotifyDataChanged() {
        List<EventTask> tasks = new ArrayList<>();
        for (int key : mItemSelectedStatus.keySet()) {
            if (mItemSelectedStatus.get(key)) {
                tasks.add(mFinishedTasks.get(key));
            }
        }
        if (tasks.size() > 0) {
            for (int i = 0; i < tasks.size(); ++i) {
                mHelper.delete(tasks.get(i));
            }
        }
        isShowCheckBox = false;
        mAdapter.customDeleteNotifyDataSetChanged();
    }


    private void init() {
        getData();
        mRecyclerView = mActivityFinishedTaskBinding.finRecycler;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new TaskDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new FinishedTaskRecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    public void getData() {
        mFinishedTasks.clear();
        RealmResults<EventTask> tasks = mHelper.getRealm()
                .where(EventTask.class).findAllSorted(EventTask.TASK_STATUS, false);
        for (EventTask task : tasks) {
            if (task.getTaskStatus() == EventTask.DONE) {
                mFinishedTasks.add(task);
            }


//            // TODO test
//            mFinishedTasks.add(task);


        }
    }

    private class FinishedTaskRecyclerAdapter extends RecyclerView.Adapter<FinishedTaskViewHolder> {

        @Override
        public FinishedTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TaskListItemBinding taskListItemBinding = DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()), R.layout.task_list_item, parent, false);

            return new FinishedTaskViewHolder(taskListItemBinding);
        }


        public void customDeleteNotifyDataSetChanged() {
            mItemSelectedStatus.clear();
            getData();
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(FinishedTaskViewHolder holder, int position) {
            mItemSelectedStatus.put(position, false);
            if (isShowCheckBox) {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(false);
                holder.imageButton.setVisibility(View.INVISIBLE);
            } else {
                holder.checkBox.setVisibility(View.GONE);
                holder.checkBox.setChecked(false);
                holder.imageButton.setVisibility(View.VISIBLE);
            }
            holder.fillItemData(mFinishedTasks.get(position));
            holder.relativeLayout.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mFinishedTasks.size();
        }
    }

    private class FinishedTaskViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, View.OnClickListener {

        private TaskListItemBinding taskListItemBinding;
        private ImageButton imageButton;
        private CheckBox checkBox;
        private RelativeLayout relativeLayout;

        public FinishedTaskViewHolder(TaskListItemBinding taskListItemBinding) {
            super(taskListItemBinding.getRoot());
            this.taskListItemBinding = taskListItemBinding;
            imageButton = taskListItemBinding.imageTaskStatus;
            checkBox = taskListItemBinding.checkTaskSelect;
            relativeLayout = taskListItemBinding.relativeContainer;
            relativeLayout.setOnLongClickListener(this);
            relativeLayout.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mItemSelectedStatus.put((Integer) relativeLayout.getTag(), isChecked);
                }
            });
        }

        public void fillItemData(EventTask task) {
            taskListItemBinding.setTask(task);
            taskListItemBinding.executePendingBindings();
        }

        @Override
        public boolean onLongClick(View v) {
            if (v == relativeLayout) {
                showMenuItem(true);
                return true;
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            if (v == relativeLayout) {
                boolean status = checkBox.isChecked();
                checkBox.setChecked(!status);
                mItemSelectedStatus.put((Integer) relativeLayout.getTag(), !status);
            }
        }
    }

    /**
     * control menu list visible or not
     *
     * @param isShow
     */

    private void showMenuItem(boolean isShow) {
//        mMenuItemSure.setVisible(isShow);
        mMenuItemCancel.setVisible(isShow);
        isShowCheckBox = isShow;
        mAdapter.notifyDataSetChanged();
    }

}
