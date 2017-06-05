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

public class PendingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView mTv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pending, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_pending);
        mTv = (TextView) mView.findViewById(R.id.tv_pending);
        mTv.setText("待处理");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new PendingAdapter());
        return mView;
    }

    /**
     * Just for test
     */
    private class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingHolder>{
        @Override
        public PendingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PendingHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_sprint, parent, false));
        }

        @Override
        public void onBindViewHolder(PendingHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        public class PendingHolder extends RecyclerView.ViewHolder {
            public PendingHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
