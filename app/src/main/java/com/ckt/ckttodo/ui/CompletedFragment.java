package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ckt.ckttodo.R;

/**
 * Created by hcy on 17-5-25.
 */

public class CompletedFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView mTv;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pending, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_pending);
        mTv = (TextView) mView.findViewById(R.id.tv_pending);
        mTv.setText("已完成");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new CompletedAdapter());
        return mView;
    }

    private class CompletedAdapter extends RecyclerView.Adapter<CompletedAdapter.CompletedHolder>{

        @Override
        public CompletedAdapter.CompletedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CompletedHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_sprint, parent, false));
        }

        @Override
        public void onBindViewHolder(CompletedAdapter.CompletedHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        public class CompletedHolder extends RecyclerView.ViewHolder {
            public CompletedHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
