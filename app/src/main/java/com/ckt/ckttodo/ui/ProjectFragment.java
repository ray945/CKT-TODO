package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.databinding.FragmentProjectBinding;
import com.ckt.ckttodo.databinding.ItemProjectBinding;
import com.ckt.ckttodo.databinding.ItemProjectTasksBinding;

import io.realm.RealmList;
import io.realm.RealmResults;

public class ProjectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FragmentProjectBinding binding = FragmentProjectBinding.inflate(inflater);
        RecyclerView rvProjects = binding.rvProject;
        binding.fabProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View editTextView = getActivity().getLayoutInflater().inflate(R.layout.dialog_edittext, null);
                final EditText editText = (EditText) editTextView.findViewById(R.id.new_task_name);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("新建计划")
                        .setView(editTextView)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String taskName = editText.getText().toString().trim();
                                if (!taskName.equals("")) {
                                    showToast("新建任务");
                                } else {
                                    showToast("任务不能为空");
                                }
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.create().show();
            }
        });

        RealmResults<Plan> planList = DatebaseHelper.getInstance(getContext()).findAll(Plan.class);
        ProjectListAdapter adapter = new ProjectListAdapter(planList);
        initRecyclerView(rvProjects, adapter);
        return binding.getRoot();
    }

    public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

        private RealmResults<Plan> planList;

        ProjectListAdapter(RealmResults<Plan> planList) {
            this.planList = planList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemProjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_project, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Plan plan = planList.get(position);
            holder.bind(plan);
            holder.binding.btnAddList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("带着任务参数跳转到新建任务界面");
                }
            });
            RecyclerView rvTasks = holder.binding.rvTasks;
            taskListAdapter adapter = new taskListAdapter(plan.getEventTasks());
            initRecyclerView(rvTasks, adapter);
        }

        @Override
        public int getItemCount() {
            return (planList == null) ? 0 : planList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final ItemProjectBinding binding;

            ViewHolder(ItemProjectBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(Plan plan) {
                binding.setPlan(plan);
                binding.executePendingBindings();
            }
        }
    }

    public class taskListAdapter extends RecyclerView.Adapter<taskListAdapter.ViewHolder> {

        private RealmList<EventTask> tasks;

        taskListAdapter(RealmList<EventTask> tasks) {
            this.tasks = tasks;
        }

        @Override
        public taskListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            com.ckt.ckttodo.databinding.ItemProjectTasksBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_project_tasks, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(taskListAdapter.ViewHolder holder, int position) {
            EventTask task = tasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return tasks == null ? 0 : tasks.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final com.ckt.ckttodo.databinding.ItemProjectTasksBinding binding;

            ViewHolder(com.ckt.ckttodo.databinding.ItemProjectTasksBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(EventTask task) {
                binding.setTask(task);
                binding.executePendingBindings();
            }
        }
    }

    void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
