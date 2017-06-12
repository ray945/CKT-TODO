package com.ckt.ckttodo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.PostProject;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.Result;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HttpClient;
import com.ckt.ckttodo.retrofit.ProjectService;
import com.ckt.ckttodo.util.TranserverUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hcy on 17-5-25.
 */

public class ProjectActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private ProjectAdapter mProjectAdapter;
    private DatebaseHelper mHelper;
    private List<Project> mDataOwner;
    private List<Project> mDataJoin;
    private int mUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.personal_project));
        mHelper = DatebaseHelper.getInstance(ProjectActivity.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_personal_project);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mDataJoin = new ArrayList<>();
        mDataOwner = new ArrayList<>();
        mProjectAdapter = new ProjectAdapter();
        mUserId = new User(this).getmID();
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mProjectAdapter);
        setDataAndNotifyDataChanged();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_project_show);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }

    @Override
    public void onRefresh() {

        ProjectService projectService = HttpClient.getHttpService(ProjectService.class);
        User user = new User(ProjectActivity.this);
        projectService.getProjects(user.getmEmail(), user.getmToken(), user.getmEmail())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<PostProject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<PostProject> value) {
                        switch (value.getResultcode()) {
                            case BeanConstant.SUCCESS_RESULT_CODE:
                                saveAndNotifyDataChange(value.getData());
                                break;
                            case BeanConstant.USER_STATUS_INVALID_ERRO_RESULT_CODE:
                                Toast.makeText(ProjectActivity.this, getString(R.string.login_status_timeout), Toast.LENGTH_SHORT).show();
                                break;
                            case BeanConstant.PASS_DATA_INVALID_RESULT_CODE:
                                Toast.makeText(ProjectActivity.this, getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                                break;
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

    private void saveAndNotifyDataChange(List<PostProject> resultData) {

        if (resultData.size() == 0) {
            return;
        }
        List<Project> insertList = new ArrayList<>();
        List<Project> updateList = new ArrayList<>();
        for (PostProject postProject : resultData) {
            Project project = mHelper.getRealm().where(Project.class).contains("projectId", postProject.getProjectId()).findFirst();
            if (project == null) {
                insertList.add(TranserverUtil.transProject(mHelper, postProject));
            } else {
                updateList.add(TranserverUtil.transProject(mHelper, postProject));
            }
        }
        mHelper.update(updateList);
        mHelper.insert(insertList);
        setDataAndNotifyDataChanged();
    }

    public void setDataAndNotifyDataChanged() {
        List<Project> projects = mHelper.findAll(Project.class);
        mDataJoin.clear();
        mDataOwner.clear();
        for (Project project : projects) {
            if (project.getOwnerId() == mUserId) {
                mDataOwner.add(project);
            } else {
                mDataJoin.add(project);
            }
        }
        mProjectAdapter.notifyDataSetChanged();
    }

    public class ProjectAdapter extends SectionedRecyclerViewAdapter<ProjectAdapter.MainVH> {


        @Override
        public int getSectionCount() {
            return 2;
        }

        @Override
        public int getItemCount(int section) {
            switch (section) {
                case 0:
                    return mDataJoin.size();
                case 1:
                    return mDataOwner.size();
                default:
                    return 0;
            }
        }

        @Override
        public void onBindHeaderViewHolder(MainVH holder, int section, boolean expanded) {
            switch (section) {
                case 0:
                    holder.title.setText("我参与的项目");
                    break;
                case 1:
                    holder.title.setText("我拥有的项目");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onBindViewHolder(MainVH holder, int section, int relativePosition, int absolutePosition) {
            if (section == 0) {
                holder.projectTitle.setText(mDataJoin.get(relativePosition).getProjectTitle());
            } else {
                holder.projectTitle.setText(mDataOwner.get(relativePosition).getProjectTitle());
            }

        }

        @Override
        public int getItemViewType(int section, int relativePosition, int absolutePosition) {
            return super.getItemViewType(section, relativePosition, absolutePosition);
        }

        @Override
        public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    layout = R.layout.item_persoanl_project_header;
                    break;
                case VIEW_TYPE_ITEM:
                    layout = R.layout.item_personal_project;
                    break;
                default:
                    layout = R.layout.item_personal_project;
                    break;
            }

            View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new MainVH(v, this);
        }

        class MainVH extends SectionedViewHolder {

            final ProjectAdapter adapter;
            final TextView title;
            TextView projectTitle;

            MainVH(View itemView, ProjectAdapter adapter) {
                super(itemView);
                this.adapter = adapter;
                this.title = (TextView) itemView.findViewById(R.id.header);
                this.projectTitle = (TextView) itemView.findViewById(R.id.project_own_tv);
            }

        }

    }

}
