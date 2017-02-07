package com.ckt.ckttodo.util;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.databinding.ItemProjectTasksBinding;

import io.realm.RealmList;


class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private RealmList<EventTask> tasks;
    private Context context;

    TaskListAdapter(Context context, RealmList<EventTask> tasks) {
        this.tasks = tasks;
        this.context = context;
    }

    TaskListAdapter(Context context){
        this.context = context;
    }

    void setTasks(RealmList<EventTask> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    void clear(){
        tasks.clear();
        notifyDataSetChanged();
    }

    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemProjectTasksBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_project_tasks, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TaskListAdapter.ViewHolder holder, int position) {
        EventTask task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
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
