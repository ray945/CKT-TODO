package com.ckt.ckttodo.ui;

import android.content.Context;
import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.databinding.FragmentTaskBinding;
import com.ckt.ckttodo.databinding.TaskListItemBinding;
import com.ckt.ckttodo.util.TranserverUtil;
import com.ckt.ckttodo.widgt.TaskDividerItemDecoration;
import com.ckt.ckttodo.widgt.TimeWatchDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

/**
 * Created by mozre
 */
public class TaskFragment extends Fragment {

    private FragmentTaskBinding mFragmentTaskBinding;
    private RecyclerView mRecyclerView;
    private TaskRecyclerViewAdapter mAdapter;
    private RealmResults<EventTask> mTasks;
    private LinkedList<EventTask> mShowTasks;
    private LinkedList<EventTask> mTopTasks = new LinkedList<>();
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
        screenTask();
        mFragmentTaskBinding = FragmentTaskBinding.inflate(inflater);
        mRecyclerView = mFragmentTaskBinding.recyclerTaskList;
        mAdapter = new TaskRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new TaskDividerItemDecoration(getContext(),
                TaskDividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        return mFragmentTaskBinding.getRoot();
    }

    private void screenTask() {
        if (mShowTasks == null) {
            mShowTasks = new LinkedList<>();
        }
        mShowTasks.clear();
        mTasks = mHelper.findAll(EventTask.class);
        mTopTasks.clear();
        int i = 0;
        for (EventTask task : mTasks) {
            if (task.getTaskStatus() != EventTask.DONE) {
                if (task.getTopNumber() > 0) {
                    mTopTasks.add(task);
                    continue;
                }
                mShowTasks.addLast(task);
            }
        }
        sortTop(mTopTasks);
    }

    private void sortTop(LinkedList<EventTask> list) {
        if (list.size() == 0) {
            return;
        }
        if (list.size() == 1) {
            mShowTasks.addFirst(list.get(0));
            return;
        }
        EventTask tmpTask;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); ++j) {
                if (list.get(j).getTopNumber() > list.get(i).getTopNumber()) {
                    tmpTask = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, tmpTask);
                }
            }
        }
        mShowTasks.addAll(0, list);

    }

    public void notifyData() {
        screenTask();
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
            resetItemSelectStatus(mItemsSelectStatus);
            notifyDataSetChanged();
        }


        /**
         * if delete data,show update mTasks data
         */

        public void customDeleteNotifyDataSetChanged() {
            screenTask();
            resetItemSelectStatus(mItemsSelectStatus);
            notifyDataSetChanged();
        }


        private void resetItemSelectStatus(Map<Integer, Boolean> map) {
            map.clear();
            for (int i = 0; mShowTasks.size() > i; ++i) {
                map.put(i, false);
            }

        }

        @Override
        public void onBindViewHolder(TaskRecyclerViewHolder holder, int position) {

            holder.setData(mShowTasks.get(position));
            holder.container.setTag(position);
            if (isShowCheckBox) {
                holder.checkBox.setChecked(mItemsSelectStatus.get(position));
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.imageButtonStatus.setVisibility(View.INVISIBLE);
                holder.textViewToTop.setVisibility(View.VISIBLE);
                if (mShowTasks.get(position).getTopNumber() > 0) {
                    holder.textViewToTop.setText(getResources().getString(R.string.cancel_top));
                }

            } else {
                holder.checkBox.setVisibility(View.GONE);
                holder.textViewToTop.setVisibility(View.GONE);
                holder.imageButtonStatus.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(false);
            }
        }

        @Override
        public int getItemCount() {
            return mShowTasks.size();
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
        EventTask mTask;
        TextView textViewToTop;
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
            textViewToTop = binding.textTaskListTop;
            container.setOnLongClickListener(this);
            container.setOnClickListener(this);
            imageButtonStatus.setOnClickListener(this);
            textViewToTop.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mItemsSelectStatus.put((Integer) container.getTag(), isChecked);
                }
            });

        }


        public void setData(EventTask data) {
            this.mTask = data;
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
                if (isShowCheckBox) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        mItemsSelectStatus.put((Integer) container.getTag(), false);
                    } else {
                        checkBox.setChecked(true);
                        mItemsSelectStatus.put((Integer) container.getTag(), true);
                    }
                } else {
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtra(TaskDetailActivity.EVENT_TASK_ID, mTask.getTaskId());
                    startActivityForResult(intent, MainActivity.MAIN_TO_TASK_DETAIL_CODE);
                }
            } else if (v == imageButtonStatus) {
                imageButtonStatus.setSelected(true);
                Intent intent = new Intent(getActivity(), ClockAnimationActivity.class);
                MainActivity activity = (MainActivity) getActivity();
                activity.transitionTo(intent);
               /* showTomatoDialog();*/
            } else if (v == textViewToTop) {
                int position = (Integer) container.getTag();
                if (mShowTasks.get(position).getTopNumber() > 0) {
                    setTaskCancelTop(position);
                } else {
                    setTaskToTop(position);
                }
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

    private void setTaskCancelTop(int position) {

        EventTask eventTask = copyTask(mShowTasks.get(position));
        eventTask.setTopNumber(EventTask.TOP_NORMAL);
        mHelper.update(eventTask);
        mShowMenuItem.setShowMenuItem(false);
        isShowCheckBox = false;
        mAdapter.customDeleteNotifyDataSetChanged();

    }


    /**
     * set task to top
     *
     * @param position
     */
    private void setTaskToTop(Integer position) {
        List<EventTask> adjustList = null;
        EventTask newTopTask = copyTask(mShowTasks.get(position));
        newTopTask.setTopNumber(EventTask.TOP_THREE);
        adjustList = adjustOrder(mShowTasks.get(position).getTopNumber());
        adjustList.add(newTopTask);
        for (int i = 0; i < adjustList.size(); ++i) {
            mHelper.update(adjustList.get(i));
        }
        mShowMenuItem.setShowMenuItem(false);
        isShowCheckBox = false;
        mAdapter.customDeleteNotifyDataSetChanged();
    }

    private List<EventTask> adjustOrder(Integer topNumber) {
        List<EventTask> tmpList = new ArrayList<>();
        EventTask tmpTask;
        EventTask resultTask = null;
        for (int i = 0; i < mTopTasks.size(); ++i) {
            tmpTask = mTopTasks.get(i);
            if (tmpTask.getTopNumber() == topNumber) {
                continue;
            }
            if (tmpTask.getTopNumber() > 0) {
                resultTask = copyTask(tmpTask);
                if (resultTask.getTopNumber() == EventTask.TOP_ONE) {
                    resultTask.setTopNumber(EventTask.TOP_NORMAL);

                } else {

                    resultTask.setTopNumber(tmpTask.getTopNumber() - 1);
                }
                tmpList.add(resultTask);
            }
        }

        return tmpList;
    }


    private EventTask copyTask(EventTask tmpTask) {
        EventTask result = new EventTask();
        result.setTaskId(tmpTask.getTaskId());
        result.setPlan(tmpTask.getPlan());
        result.setCreateUerId(tmpTask.getCreateUerId());
        result.setTaskContent(tmpTask.getTaskContent());
        result.setTaskPredictTime(tmpTask.getTaskPredictTime());
        result.setTaskPriority(tmpTask.getTaskPriority());
        result.setTaskStatus(tmpTask.getTaskStatus());
        result.setTaskTitle(tmpTask.getTaskTitle());
        result.setTaskRealSpendTime(tmpTask.getTaskRealSpendTime());
        result.setTaskUpdateTime(tmpTask.getTaskUpdateTime());
        result.setExecUserId(tmpTask.getExecUserId());
        result.setPlanId(tmpTask.getPlanId());
        result.setTaskRemindTime(tmpTask.getTaskRemindTime());
        result.setTaskStartTime(tmpTask.getTaskStartTime());
        result.setTaskType(tmpTask.getTaskType());
        result.setTopNumber(tmpTask.getTopNumber());
        return result;
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
                    tasks.add(mShowTasks.get(position));
                }
            }
            for (EventTask task1 : tasks) {
                mHelper.delete(task1);
            }
            mAdapter.customDeleteNotifyDataSetChanged();
        }

        mAdapter.customNotifyDataSetChanged();
    }

    public void finishTaskAction() {
        isShowCheckBox = false;
        List<EventTask> tasks = new ArrayList<>();
        for (int position : mItemsSelectStatus.keySet()) {
            if (mItemsSelectStatus.get(position)) {
                tasks.add(mShowTasks.get(position));
            }
        }
        EventTask upDateTask = new EventTask();
        for (EventTask task1 : tasks) {
            TranserverUtil.transEventTask(upDateTask, task1);
            upDateTask.setTaskStatus(EventTask.DONE);
            mHelper.update(upDateTask);
        }
        mAdapter.customDeleteNotifyDataSetChanged();
    }


    public interface ShowMainMenuItem {
        void setShowMenuItem(boolean isShow);
    }

}
