package com.ckt.ckttodo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.EventTask;
import com.ckt.ckttodo.database.PostProject;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.Result;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HTTPService;
import com.ckt.ckttodo.network.HttpClient;
import com.ckt.ckttodo.network.HttpConstants;
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

    public static final int PROJECT_NEW_REQUEST_CODE = 100;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private ProjectAdapter mProjectAdapter;
    private Transition transition;
    private DatebaseHelper mHelper;
    private List<Project> mDataOwner;
    private List<Project> mDataJoin;
    private int mUserId;

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
        mHelper = DatebaseHelper.getInstance(ProjectActivity.this);
        mDataJoin = new ArrayList<>();
        mDataOwner = new ArrayList<>();
        mUserId = new User(this).getmID();
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
        mProjectAdapter = new ProjectAdapter();
        mProjectAdapter.shouldShowHeadersForEmptySections(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mProjectAdapter);
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

    private void setupWindowAnimations() {
        transition = buildEnterTransition();
        getWindow().setEnterTransition(transition);
    }

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
                        doDeleteRequest(project, position, section);
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

    private void doDeleteRequest(final Project project, final int position, final int section) {
        User user = new User(this);
        HttpClient.getHttpService(ProjectService.class).deleteProject(user.getmEmail(), user.getmToken(), project.getProjectId())
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
                                mHelper.delete(project);
                                if (section == 0) {
                                    mDataOwner.remove(project);
                                } else {
                                    mDataJoin.remove(project);
                                }
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
                       setDataAndNotifyDataChanged();
                    }
                });


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
                    return mDataOwner.size();
                case 1:
                    return mDataJoin.size();
                default:
                    return 0;
            }
        }

        @Override
        public void onBindHeaderViewHolder(MainVH holder, int section, boolean expanded) {
            switch (section) {
                case 0:
                    holder.tvHeader.setText(getResources().getText(R.string.owner_project));
                    break;
                case 1:
                    holder.tvHeader.setText(getResources().getText(R.string.join_project));
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onBindViewHolder(MainVH holder, final int section, final int relativePosition, int absolutePosition) {
            if (section == 0) {
                holder.tvItem.setText(mDataOwner.get(relativePosition).getProjectTitle());
            } else {
                holder.tvItem.setText(mDataJoin.get(relativePosition).getProjectTitle());
            }
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectActivity.this, DetailProActivity.class);
                    startActivity(intent);
                }
            });
            holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteProject(relativePosition, section);
                    return false;
                }
            });
        }

        @Override
        public int getItemViewType(int section, int relativePosition, int absolutePosition) {
            return super.getItemViewType(section, relativePosition, absolutePosition);
        }

        @Override
        public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout = 0;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    layout = R.layout.item_persoanl_project_header;
                    break;
                case VIEW_TYPE_ITEM:
                    layout = R.layout.item_personal_project;
                    break;
                default:
                    break;
            }

            View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new MainVH(v, this);
        }

        class MainVH extends SectionedViewHolder {
            final ProjectAdapter adapter;
            final TextView tvHeader;
            final TextView tvItem;
            final RelativeLayout relativeLayout;

            MainVH(View itemView, ProjectAdapter adapter) {
                super(itemView);
                this.adapter = adapter;
                this.tvHeader = (TextView) itemView.findViewById(R.id.tv_header);
                this.tvItem = (TextView) itemView.findViewById(R.id.tv_item);
                this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_container);
            }

        }

    }
}
