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

public class OngoingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView mTv;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pending, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_pending);
        mTv = (TextView) mView.findViewById(R.id.tv_pending);
        mTv.setText("进行中");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new OngoingAdapter());
        return mView;
    }

    private class OngoingAdapter extends RecyclerView.Adapter<OngoingAdapter.OngoingHolder>{

        @Override
        public OngoingAdapter.OngoingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OngoingHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_sprint, parent, false));
        }

        @Override
        public void onBindViewHolder(OngoingAdapter.OngoingHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        public class OngoingHolder extends RecyclerView.ViewHolder {
            public OngoingHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
