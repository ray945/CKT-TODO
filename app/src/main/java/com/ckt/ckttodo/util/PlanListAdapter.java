package com.ckt.ckttodo.util;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.ItemProjectPlansBinding;

import java.text.SimpleDateFormat;

import io.realm.RealmList;

/**
 * Created by zhiwei.li
 */

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder> implements View.OnClickListener {

    private RealmList<Plan> plans;
    private Context context;

    public PlanListAdapter(Context context, RealmList<Plan> plans) {
        this.plans = plans;
        this.context = context;
    }

    public PlanListAdapter(Context context) {
        this.context = context;
    }

    public void setPlans(RealmList<Plan> plans) {
        this.plans = plans;
        notifyDataSetChanged();
    }

    public void clear() {
        plans.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemProjectPlansBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_project_plans, parent, false);
        binding.getRoot().setOnClickListener(this);
        return new ViewHolder(binding);
    }

    private OnItemClickListener onItemClickListener;

    public static interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        Plan plan = plans.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
        holder.binding.tvPlanTime.setText(dateFormat.format(plan.getStartTime()) + " - " + dateFormat.format(plan.getEndTime()));
        holder.bind(plan);
    }

    @Override
    public int getItemCount() {
        return plans == null ? 0 : plans.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick((Integer) v.getTag(), v);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemProjectPlansBinding binding;

        ViewHolder(ItemProjectPlansBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Plan plan) {
            binding.setPlan(plan);
            binding.executePendingBindings();
        }
    }
}
