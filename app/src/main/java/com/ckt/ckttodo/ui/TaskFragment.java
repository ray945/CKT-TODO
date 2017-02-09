package com.ckt.ckttodo.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.databinding.FragmentTaskBinding;
import com.ckt.ckttodo.databinding.TaskListItemBinding;
import com.ckt.ckttodo.widgt.TaskDividerItemDecoration;
import com.ckt.ckttodo.widgt.TimeWatchDialog;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmResults;

public class TaskFragment extends Fragment {

    private FragmentTaskBinding mFragmentTaskBinding;
    private RecyclerView mRecyclerView;
    private TaskRecyclerViewAdapter mAdapter;
    private RealmResults<EventTask> mTasks;
    private static boolean isShowCheckBox = false;
    private Map<Integer, Boolean> mItemsSelectStatus = new HashMap<>();
    private ShowMainMenuItem mShowMenuItem;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            MainActivity activity = (MainActivity) context;
            this.mShowMenuItem = (ShowMainMenuItem) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Cast Exception");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return init(inflater);
    }

    private View init(LayoutInflater inflater) {
        mTasks = DatebaseHelper.getInstance(getContext()).findAll(EventTask.class);
        mFragmentTaskBinding = FragmentTaskBinding.inflate(inflater);
        mRecyclerView = mFragmentTaskBinding.recyclerTaskList;
        mAdapter = new TaskRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new TaskDividerItemDecoration(getContext(),
                TaskDividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        return mFragmentTaskBinding.getRoot();
    }


    private class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewHolder> {

        @Override
        public TaskRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TaskListItemBinding taskListItemBinding = DataBindingUtil.inflate(LayoutInflater.
                    from(getContext()), R.layout.task_list_item, parent, false);


            return new TaskRecyclerViewHolder(taskListItemBinding);
        }

        /**
         * 每次数据改动时都需要清空存储在map中的数据，在重绘调用onBindViewHolder时初始化map数据
         */
        public void customNotifyDataSetChanged() {
            mItemsSelectStatus.clear();
            notifyDataSetChanged();
        }


        @Override
        public void onBindViewHolder(TaskRecyclerViewHolder holder, int position) {

//            holder.setData(mTasks.get(position));
            holder.container.setTag(position);
            mItemsSelectStatus.put(position, isShowCheckBox);
            if (isShowCheckBox) {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.imageButtonStatus.setVisibility(View.INVISIBLE);
            } else {
                holder.checkBox.setVisibility(View.GONE);
                holder.checkBox.setChecked(false);
                holder.imageButtonStatus.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }


    private class TaskRecyclerViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, View.OnClickListener {
        private TimeWatchDialog timeWatchDialog;
        RelativeLayout container;
        TextView textViewPlan;
        TextView textViewPlanTime;
        TextView textViewSpendTime;
        ImageButton imageButtonStatus;
        CheckBox checkBox;
        private TaskListItemBinding mBinding;

        public TaskRecyclerViewHolder(TaskListItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            textViewPlan = binding.textTaskListPlan;
            textViewPlanTime = binding.textTaskListPlanTime;
            textViewSpendTime = binding.textTaskListTakeTime;
            imageButtonStatus = binding.imageTaskStatus;
            checkBox = binding.checkTaskSelect;
            container = binding.relativeContainer;
            container.setOnLongClickListener(this);
            container.setOnClickListener(this);
            imageButtonStatus.setOnClickListener(this);

        }


        public void setData(EventTask data) {
            mBinding.setTask(data);
            mBinding.executePendingBindings();
        }

        @Override
        public boolean onLongClick(View v) {
            if (v == container) {
                itemContainerLongClickedEvent();
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            if (v == container) {
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    mItemsSelectStatus.put((Integer) container.getTag(), false);
                } else {
                    checkBox.setChecked(true);
                    mItemsSelectStatus.put((Integer) container.getTag(), true);
                }
            } else if (v == imageButtonStatus) {
                imageButtonStatus.setSelected(true);
                Log.d("TTT", "onClick: " + container.getTag());
                showTomatoDialog();


            }
        }

        /**
         * 展示番茄时钟对话框
         * 显示任务状态
         * 结束番茄时钟后更新花费时间
         */
        private void showTomatoDialog() {
            if (timeWatchDialog == null) {
                timeWatchDialog = new TimeWatchDialog(getContext());
                timeWatchDialog.setOnCancelClickedListener(new TimeWatchDialog.CancelClickedListener() {
                    @Override
                    public void onCancelClickedListener() {
                        imageButtonStatus.setSelected(false);
                        long spendTime = timeWatchDialog.stop();
                        Log.d("TTT", "onCancelClickedListener: " + spendTime);
                    }
                });
            }
            timeWatchDialog.show();
            timeWatchDialog.start();

        }
    }


    /**
     * 控制复选框是否口见
     */
    private void itemContainerLongClickedEvent() {
        isShowCheckBox = true;
        mShowMenuItem.setShowMenuItem(true);
        mAdapter.customNotifyDataSetChanged();
    }

    /**
     * 删除长按后，用户选中的项或撤销删除
     *
     * @param isDelete
     */
    public void finishDeleteAction(boolean isDelete) {
        isShowCheckBox = false;
        mAdapter.customNotifyDataSetChanged();
    }


    public interface ShowMainMenuItem {
        void setShowMenuItem(boolean isShow);
    }

}
