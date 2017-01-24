package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Plan;
import com.ckt.ckttodo.util.ChartManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

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
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        RecyclerView rv_project = (RecyclerView) view.findViewById(R.id.rv_project);
        FloatingActionButton fab_project = (FloatingActionButton) view.findViewById(R.id.fab_project);
        fab_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View editTextView = getActivity().getLayoutInflater().inflate(R.layout.dialog_edittext,null);
                final EditText editText = (EditText) editTextView.findViewById(R.id.new_task_name);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("新建计划")
                        .setView(editTextView)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String taskName =editText.getText().toString().trim();
                                if (!taskName.equals("")){
                                    Toast.makeText(getContext(),"带着任务参数跳转到新建任务界面",Toast.LENGTH_SHORT).show();
                                }else {
                                    return;
                                }
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.create().show();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        rv_project.setLayoutManager(layoutManager);
        RealmResults<Plan> planList = DatebaseHelper.getInstance(getContext()).findAll(Plan.class);
        ProjectListAdapter adapter = new ProjectListAdapter(planList);
        rv_project.setAdapter(adapter);
        return view;
    }

    public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

        private RealmResults<Plan> planList;

        public ProjectListAdapter(RealmResults<Plan> planList) {
            this.planList = planList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(getContext()).inflate(R.layout.item_project, parent);
            return new ViewHolder(root);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Plan plan = planList.get(position);
            holder.projectName.setText(plan.getPlanName());
            holder.projectProgress.setText(String.valueOf(plan.getAccomplishProgress()));
            holder.addListBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"跳转到新建任务",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return (planList == null) ? 0 : planList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {

            private TextView projectName;
            private TextView projectProgress;
            private RecyclerView taskList;
            private ImageButton addListBtn;

            public ViewHolder(View itemView) {
                super(itemView);
                projectName = (TextView) itemView.findViewById(R.id.tv_project_name);
                projectProgress = (TextView) itemView.findViewById(R.id.tv_project_progress);
                taskList = (RecyclerView) itemView.findViewById(R.id.rv_tasks);
                addListBtn = (ImageButton) itemView.findViewById(R.id.btn_addList);
            }
        }
    }
}
