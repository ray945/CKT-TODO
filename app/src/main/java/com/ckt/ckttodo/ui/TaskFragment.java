package com.ckt.ckttodo.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.databinding.FragmentTaskBinding;
import com.ckt.ckttodo.databinding.TaskListItemBinding;

import io.realm.RealmResults;

public class TaskFragment extends Fragment {

    private FragmentTaskBinding mFragmentTaskBinding;
    private RecyclerView mRecyclerView;
    private TaskRecyclerViewAdapter mAdapter;
    private RealmResults<EventTask> mTasks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return init(inflater);
    }

    private View init(LayoutInflater inflater) {
        mFragmentTaskBinding = FragmentTaskBinding.inflate(inflater);
        mRecyclerView = mFragmentTaskBinding.recyclerTaskList;
        mAdapter = new TaskRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

        @Override
        public void onBindViewHolder(TaskRecyclerViewHolder holder, int position) {
//            holder.setData(mTasks.get(position));
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }


    private class TaskRecyclerViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView textViewPlan;
        TextView textViewPlanTime;
        TextView textViewSpendTime;
        ImageButton imageButtonStatus;
        private TaskListItemBinding mBinding;

        public TaskRecyclerViewHolder(TaskListItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            textViewPlan = binding.textTaskListPlan;
            textViewPlanTime = binding.textTaskListPlanTime;
            textViewSpendTime = binding.textTaskListTakeTime;
            imageButtonStatus = binding.imageTaskStatus;
            container = binding.relativeContainer;
        }


        public void setData(EventTask data) {
            mBinding.setTask(data);
            mBinding.executePendingBindings();
        }
    }

}
