package com.ckt.ckttodo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.PostTask;
import com.ckt.ckttodo.database.Result;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.databinding.FragmentTaskBinding;
import com.ckt.ckttodo.databinding.TaskListItemBinding;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HttpClient;
import com.ckt.ckttodo.retrofit.TaskService;
import com.ckt.ckttodo.util.TranserverUtil;
import com.ckt.ckttodo.widgt.TaskDividerItemDecoration;
import com.ckt.ckttodo.widgt.TimeWatchDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;
import retrofit2.Retrofit;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mozre
 */
public class TaskFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentTaskBinding mFragmentTaskBinding;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TaskRecyclerViewAdapter mAdapter;
    private RealmResults<EventTask> mTasks;
    private LinkedList<EventTask> mShowTasks;
    private LinkedList<EventTask> mTopTasks = new LinkedList<>();
    private static boolean isShowCheckBox = false;
    private Map<Integer, Boolean> mItemsSelectStatus = new HashMap<>();
    private ShowMainMenuItem mShowMenuItem;
    private DatebaseHelper mHelper;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

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
        mSwipeRefreshLayout = mFragmentTaskBinding.swipeTask;
        mSwipeRefreshLayout.setOnRefreshListener(this);
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
        mAdapter.customNotifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        User user = new User(getContext());
        HttpClient.getHttpService(TaskService.class).getTasksById(user.getmEmail(), user.getmToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<PostTask>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<PostTask> value) {
                        switch (value.getResultcode()) {
                            case BeanConstant.SUCCESS_RESULT_CODE:
                                saveAndNotifyDataChanged(value.getData());
                                break;
                            case BeanConstant.USER_STATUS_INVALID_ERRO_RESULT_CODE:
                                Toast.makeText(getContext(), getString(R.string.login_status_timeout), Toast.LENGTH_SHORT).show();
                                break;
                            case BeanConstant.PASS_DATA_INVALID_RESULT_CODE:
                                Toast.makeText(getContext(), getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });

    }

    private void saveAndNotifyDataChanged(List<PostTask> resultData) {
        List<EventTask> insertList = new ArrayList<>();
        List<EventTask> updateList = new ArrayList<>();
        EventTask task;
        for (PostTask postTask : resultData) {
            task = mHelper.getRealm().where(EventTask.class).contains("taskId", postTask.getTaskId()).findFirst();
            if (task == null) {
                insertList.add(TranserverUtil.transTask(postTask));
            } else {
                updateList.add(TranserverUtil.transTask(postTask));
            }
        }
        mHelper.update(updateList);
        mHelper.insert(insertList);
        notifyData();
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
                    Drawable drawableTop = getResources().getDrawable(R.drawable.ic_vertical_align_bottom_black_48dp);
                    holder.textViewToTop.setText(getResources().getString(R.string.cancel_top));
                    holder.textViewToTop.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
                } else {
                    Drawable drawableTop = getResources().getDrawable(R.drawable.ic_vertical_align_top_black_48dp);
                    holder.textViewToTop.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
                    holder.textViewToTop.setText(getResources().getString(R.string.to_top));
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
//        mAdapter.customDeleteNotifyDataSetChanged();
        notifyData();
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

    public void finishDeleteAction(final boolean isDelete) {
        isShowCheckBox = false;
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.info))
                .setMessage(getResources().getString(R.string.notice_delete))
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.customNotifyDataSetChanged();
                    }
                })
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setCancelable(false)
                .create().show();
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
