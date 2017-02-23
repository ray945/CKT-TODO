package com.ckt.ckttodo.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

/**
 *
 * Created by mozre
 *
 */
public class TaskFragment extends Fragment {

    private FragmentTaskBinding mFragmentTaskBinding;
    private RecyclerView mRecyclerView;
    private TaskRecyclerViewAdapter mAdapter;
    private RealmResults<EventTask> mTasks;
    private static boolean isShowCheckBox = false;
    private Map<Integer, Boolean> mItemsSelectStatus = new HashMap<>();
    private ShowMainMenuItem mShowMenuItem;
    private DatebaseHelper mHelper;

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
        mHelper = DatebaseHelper.getInstance(getContext());
        mTasks = mHelper.findAll(EventTask.class);
        mFragmentTaskBinding = FragmentTaskBinding.inflate(inflater);
        mRecyclerView = mFragmentTaskBinding.recyclerTaskList;
        mAdapter = new TaskRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new TaskDividerItemDecoration(getContext(),
                TaskDividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        return mFragmentTaskBinding.getRoot();
    }

    public void notifyData() {
        mTasks = mHelper.findAll(EventTask.class);
        mAdapter.notifyDataSetChanged();
    }


    private class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewHolder> {

        @Override
        public TaskRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TaskListItemBinding taskListItemBinding = DataBindingUtil.inflate(LayoutInflater.
                    from(getContext()), R.layout.task_list_item, parent, false);


            return new TaskRecyclerViewHolder(taskListItemBinding);
        }

        /**
         * clear mItemsSelectStatus before notifyDataSetChanged
         */

        public void customNotifyDataSetChanged() {
            mItemsSelectStatus.clear();
            notifyDataSetChanged();
        }

        /**
         * if delete data,show update mTasks data
         */

        public void customDeleteNotifyDataSetChanged() {
            mItemsSelectStatus.clear();
            mTasks = mHelper.findAll(EventTask.class);
            notifyDataSetChanged();
        }


        @Override
        public void onBindViewHolder(TaskRecyclerViewHolder holder, int position) {

            holder.setData(mTasks.get(position));
            holder.container.setTag(position);
            mItemsSelectStatus.put(position, false);
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
            return mTasks.size();
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
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mItemsSelectStatus.put((Integer) container.getTag(), isChecked);
                }
            });

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
                Intent intent = new Intent(getActivity(),ClockAnimationActivity.class);
                MainActivity activity = (MainActivity) getActivity();
                activity.transitionTo(intent);
               /* showTomatoDialog();*/


            }
        }

        /**
         * show about tomato time
         */
        private void showTomatoDialog() {
            if (timeWatchDialog == null) {
                timeWatchDialog = new TimeWatchDialog(getContext());
                timeWatchDialog.setOnCancelClickedListener(new TimeWatchDialog.CancelClickedListener() {
                    @Override
                    public void onCancelClickedListener() {
                        imageButtonStatus.setSelected(false);
                        timeWatchDialog.stop();
//                        long spendTime = timeWatchDialog.stop();
//                        Log.d("TTT", "onCancelClickedListener: " + spendTime);
                    }
                });
            }
            timeWatchDialog.show();
            timeWatchDialog.start();

        }
    }


    /**
     * control about the delete checkbox visible or not
     */

    private void itemContainerLongClickedEvent() {
        isShowCheckBox = true;
        mShowMenuItem.setShowMenuItem(true);
        mAdapter.customNotifyDataSetChanged();
    }

    /**
     * control delete task listl
     *
     * @param isDelete
     */

    public void finishDeleteAction(boolean isDelete) {
        isShowCheckBox = false;
        if (isDelete) {
            List<EventTask> tasks = new ArrayList<>();
            for (int position : mItemsSelectStatus.keySet()) {
                if (mItemsSelectStatus.get(position)) {
                    tasks.add(mTasks.get(position));
                }
            }
            for (EventTask task1 : tasks) {
                mHelper.delete(task1);
            }
            mAdapter.customDeleteNotifyDataSetChanged();
        }

        mAdapter.customNotifyDataSetChanged();
    }


    public interface ShowMainMenuItem {
        void setShowMenuItem(boolean isShow);
    }

}
