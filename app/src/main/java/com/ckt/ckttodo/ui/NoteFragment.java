package com.ckt.ckttodo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ckt.ckttodo.R;

import java.util.List;

public class NoteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView lv_note;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        lv_note = (RecyclerView) view.findViewById(R.id.rv_note);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static class NoteAdapter extends RecyclerView.Adapter implements View.OnClickListener {
        List<String> noteList;

        public NoteAdapter(List<String> noteList) {
            this.noteList = noteList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.note_item, parent, false);
            NoteViewHolder noteViewHolder = new NoteViewHolder(view);
            view.setOnClickListener(this);
            return noteViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String note = noteList.get(position);
            NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
            noteViewHolder.itemView.setTag(position);
            noteViewHolder.tv_noteTitle.setText(note);
            noteViewHolder.tv_noteContent.setText(note);
        }

        @Override
        public int getItemCount() {
            return noteList.size();
        }

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public static interface OnItemClickListener {
            void onItemClick(int position, View view);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick((Integer) v.getTag(), v);
            }
        }

        class NoteViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_noteTitle;
            public TextView tv_noteContent;

            public NoteViewHolder(View itemView) {
                super(itemView);
                tv_noteTitle = (TextView) itemView.findViewById(R.id.tv_noteTitle);
                tv_noteContent = (TextView) itemView.findViewById(R.id.tv_noteContent);
            }
        }
    }
}
