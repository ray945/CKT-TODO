package com.ckt.ckttodo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.ckt.ckttodo.R;

/**
 * Created by hcy on 17-5-25.
 */

public class ProjectActivity extends AppCompatActivity {
    private RecyclerView ownRecyclerView;
    private RecyclerView joinRecyclerView;
    private ImageButton imageButtonAdd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ownRecyclerView = (RecyclerView) findViewById(R.id.rv_project_own);
        joinRecyclerView = (RecyclerView) findViewById(R.id.rv_project_join);
        imageButtonAdd = (ImageButton) findViewById(R.id.btn_addProject);

        ownRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        joinRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ownRecyclerView.setAdapter(new OwnAdapter());
        joinRecyclerView.setAdapter(new JoinAdapter());

        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*start a new project Activity*/
            }
        });
    }

    /**
     * Just for test
     */
    private class OwnAdapter extends RecyclerView.Adapter<OwnAdapter.OwnHolder>{

        @Override
        public OwnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OwnHolder(LayoutInflater.from(ProjectActivity.this).inflate(R.layout.item_project_own, parent, false));
        }

        @Override
        public void onBindViewHolder(OwnHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        public class OwnHolder extends RecyclerView.ViewHolder {
            public OwnHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * Just for test
     */
    private class JoinAdapter extends RecyclerView.Adapter<JoinAdapter.JoinHolder>{

        @Override
        public JoinHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new JoinHolder(LayoutInflater.from(ProjectActivity.this).inflate(R.layout.item_project_own, parent, false));
        }

        @Override
        public void onBindViewHolder(JoinHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public class JoinHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private RelativeLayout mRelativeLayout;
            public JoinHolder(View itemView) {
                super(itemView);
                mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_container);
                mRelativeLayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                transitionTo(new Intent(ProjectActivity.this, DetailProActivity.class));
            }
        }
    }

    void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }
}
