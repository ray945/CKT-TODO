package com.ckt.ckttodo.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ckt.ckttodo.R;
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Project;
import com.ckt.ckttodo.database.User;
import com.ckt.ckttodo.database.UserInfo;
import com.ckt.ckttodo.network.BeanConstant;
import com.ckt.ckttodo.network.HttpClient;
import com.ckt.ckttodo.network.HttpConstants;
import com.ckt.ckttodo.retrofit.ProjectService;
import com.ckt.ckttodo.widgt.ProjectVisibilityDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class NewProjectActivity extends AppCompatActivity {

    private static final String TAG = "NewProjectActivity";
    private LinearLayout mLinearLayoutVisibility;
    private EditText mEditTextProjectName;
    private EditText mEditTextProjectDescription;
    private TextView mTextViewVisible;
    private ProgressDialog mProgressDialog;
    private int mCurrentVisibility = 0;
    private CompositeSubscription mSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.new_project));
        setContentView(R.layout.activity_new_project);
        init();
    }

    private void init() {
        mLinearLayoutVisibility = (LinearLayout) findViewById(R.id.linear_project_visibility);
        mEditTextProjectName = (EditText) findViewById(R.id.edit_new_project_name);
        mEditTextProjectDescription = (EditText) findViewById(R.id.edit_new_project_description);
        mTextViewVisible = (TextView) findViewById(R.id.text_project_visible);
        mLinearLayoutVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProjectVisibilityDialog(NewProjectActivity.this, new ProjectVisibilityDialog.VisibilityChangeListener() {
                    @Override
                    public void onVisibilityChangeListener(int currentVisibility) {
                        mCurrentVisibility = currentVisibility;
                        if (currentVisibility == Project.PROJECT_PRIVATE) {
                            mTextViewVisible.setText(getResources().getString(R.string.project_private));
                        } else {
                            mTextViewVisible.setText(getResources().getString(R.string.project_public));
                        }
                    }
                }, mCurrentVisibility).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_project, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_sure:
                checkAndCommit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkAndCommit() {
        DatebaseHelper helper = DatebaseHelper.getInstance(this);
        if (TextUtils.isEmpty(mEditTextProjectName.getText()) || mEditTextProjectName.getText().toString().replace(" ", "").length() == 0) {
            Toast.makeText(this, getResources().getString(R.string.project_is_not_null), Toast.LENGTH_SHORT).show();
            return;
        }
        Project tmpProject = helper.getRealm().where(Project.class).contains("projectTitle", mEditTextProjectName.getText().toString()).findFirst();
        if (tmpProject != null) {
            Toast.makeText(this, getResources().getString(R.string.project_is_not_null), Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.info));
        mProgressDialog.setMessage(getString(R.string.wait_minutes));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        Project project = new Project();
        project.setProjectTitle(mEditTextProjectName.getText().toString());
        if (!TextUtils.isEmpty(mEditTextProjectDescription.getText()) && !mEditTextProjectDescription.getText().toString().replace(" ", "").equals("")) {
            project.setProjectSummary(mEditTextProjectDescription.getText().toString());
        }
        project.setCreateTime(System.currentTimeMillis());
        UserInfo userInfo = helper.getRealm().where(UserInfo.class).contains("mem_email", new User(this).getmEmail()).findFirst();
        project.setUserInfo(userInfo);
        project.setProjectId(UUID.randomUUID().toString());
        project.setSync(false);
        helper.insert(project);
        // post project to service
        postProjectToService(project);

    }

    private void postProjectToService(Project project) {
        Retrofit retrofit = HttpClient.getRetrofit();
        ProjectService projectService = retrofit.create(ProjectService.class);
        JSONObject proStr = new JSONObject();
        proStr.put("project_id", project.getProjectId());
        proStr.put("project_name", project.getProjectTitle());
        if (project.getProjectSummary() != null) {
            proStr.put("projcet_description", project.getProjectSummary());
        }
        User user = new User(this);
        proStr.put("mem_id", user.getmID());
        Map<String, String> map = new HashMap<>();
        map.put("postproject", proStr.toString());
        map.put("email", user.getmEmail());
        map.put("token", user.getmToken());
        mSubscription.add(projectService.postNewProject(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        try {
                            String result = response.string();
                            JSONObject jsonResult = JSON.parseObject(result);
                            switch (jsonResult.getInteger(BeanConstant.RESULT_CODE)) {
                                case BeanConstant.SUCCESS_RESULT_CODE:
                                    finish();
                                    break;
                                case BeanConstant.USER_STATUS_INVALID_ERRO_RESULT_CODE:
                                    Toast.makeText(NewProjectActivity.this, getString(R.string.login_status_timeout), Toast.LENGTH_SHORT).show();
                                    break;
                                case BeanConstant.PASS_DATA_INVALID_RESULT_CODE:
                                    Toast.makeText(NewProjectActivity.this, getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
        );
    }
}
