package com.ckt.ckttodo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatabaseHelper;
import com.ckt.ckttodo.database.PostProject;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.Result;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HttpClient;
import com.ckt.ckttodo.retrofit.ProjectService;
import com.ckt.ckttodo.util.TranserverUtil;
import com.truizlop.sectionedrecyclerview.SimpleSectionedAdapter;

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

    public static final int PROJECT_NEW_REQUEST_CODE = 100;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private Transition transition;
    private DatabaseHelper mHelper;
    private List<Project> mDataOwner;
    private List<Project> mDataJoin;
    private int mUserId;
    private SimpleSectionAdapter mSimpleSectionAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROJECT_NEW_REQUEST_CODE && resultCode == NewProjectActivity.NEW_PROJECT_SUCCESS_RESULT_CODE) {
            setDataAndNotifyDataChanged();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        initData();
        initUI();
        setDataAndNotifyDataChanged();
    }

    private void initData() {
        mHelper = DatabaseHelper.getInstance(ProjectActivity.this);
        mDataJoin = new ArrayList<>();
        mDataOwner = new ArrayList<>();
        mUserId = new User(this).getId();
    }

    private void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.personal_project));
        if (Build.VERSION.SDK_INT >= 21) {
            setupWindowAnimations();
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_personal_project);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_project_show);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mSimpleSectionAdapter = new SimpleSectionAdapter();
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mSimpleSectionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_project, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.add_project:
                Intent intent = new Intent(ProjectActivity.this, NewProjectActivity.class);
                startActivityForResult(intent, PROJECT_NEW_REQUEST_CODE);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        transition = buildEnterTransition();
        getWindow().setEnterTransition(transition);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildEnterTransition() {
        Slide enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.setSlideEdge(Gravity.RIGHT);
        return enterTransition;
    }

    @Override
    public void onRefresh() {
        ProjectService projectService = HttpClient.getHttpService(ProjectService.class);
        User user = new User(ProjectActivity.this);
        projectService.getProjects(user.getEmail(), user.getToken(), user.getEmail())
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
        mSimpleSectionAdapter.notifyDataSetChanged();
    }


    /**
     * @param position Delete the selected item
     */
    private void deleteProject(final int position, final int section) {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.notice_delete))
                .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Project project = null;
                        if (section == 0) {
                            project = mDataOwner.get(position);
                        } else {
                            mDataJoin.get(position);
                        }
                        doDeleteRequest(project, section);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void doDeleteRequest(final Project project, final int section) {
        User user = new User(this);
        HttpClient.getHttpService(ProjectService.class).deleteProject(user.getEmail(), user.getToken(), project.getProjectId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result value) {
                        switch (value.getResultcode()) {
                            case BeanConstant.SUCCESS_RESULT_CODE:
                                if (section == 0) {
                                    mDataOwner.remove(project);
                                } else {
                                    mDataJoin.remove(project);
                                }
                                mHelper.delete(project);
                                Toast.makeText(ProjectActivity.this, getString(R.string.delete_project_success), Toast.LENGTH_SHORT).show();
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mSimpleSectionAdapter.notifyDataSetChanged();
                    }
                });


    }

    class SimpleSectionAdapter extends SimpleSectionedAdapter<SimpleSectionAdapter.SimpleItemViewHolder> {

        @Override
        protected String getSectionHeaderTitle(int section) {
            return section == 0 ? "我拥有的项目" : "我参与的项目";
        }

        @Override
        protected int getSectionCount() {
            return 2;
        }

        @Override
        protected int getItemCountForSection(int section) {
            if (section == 0) {
                return mDataOwner.size();
            } else {
                return mDataJoin.size();
            }
        }

        @Override
        protected SimpleItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_personal_project, parent, false);
            return new SimpleItemViewHolder(view);
        }

        @Override
        protected void onBindItemViewHolder(SimpleItemViewHolder holder, final int section, final int position) {
            if (section == 0) {
                holder.textView.setText(mDataOwner.get(position).getProjectTitle());
            } else {
                holder.textView.setText(mDataJoin.get(position).getProjectTitle());
            }
            holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    deleteProject(position, section);
                    return false;
                }
            });
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectActivity.this, DetailProActivity.class);
                    if (section == 0) {
                        intent.putExtra(Project.PROJECT_ID, mDataOwner.get(position).getProjectId());
                    } else {
                        intent.putExtra(Project.PROJECT_ID, mDataJoin.get(position).getProjectId());
                    }
                    startActivity(intent);
                }
            });
        }

        class SimpleItemViewHolder extends RecyclerView.ViewHolder {

            TextView textView;
            RelativeLayout ll;

            SimpleItemViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_item);
                ll = (RelativeLayout) itemView.findViewById(R.id.relative_container);
            }

        }
    }
}
