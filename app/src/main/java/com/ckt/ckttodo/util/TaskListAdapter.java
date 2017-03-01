package com.ckt.ckttodo.util;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.databinding.ItemProjectTasksBinding;
import com.ckt.ckttodo.ui.NoteFragment;

import io.realm.RealmList;

/**
 * Created by zhiwei.li
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> implements View.OnClickListener {

    private RealmList<EventTask> tasks;
    private Context context;

    public TaskListAdapter(Context context, RealmList<EventTask> tasks) {
        this.tasks = tasks;
        this.context = context;
    }

    public TaskListAdapter(Context context) {
        this.context = context;
    }

    void setTasks(RealmList<EventTask> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    void clear() {
        tasks.clear();
        notifyDataSetChanged();
    }

    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemProjectTasksBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_project_tasks, parent, false);
        binding.getRoot().setOnClickListener(this);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TaskListAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        EventTask eventTask = tasks.get(position);
        holder.bind(eventTask);
    }

    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
    }

    private NoteFragment.NoteAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(NoteFragment.NoteAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick((Integer) v.getTag(), v);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemProjectTasksBinding binding;

        ViewHolder(ItemProjectTasksBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(EventTask task) {
            binding.setTask(task);
            binding.executePendingBindings();
        }
    }
}
