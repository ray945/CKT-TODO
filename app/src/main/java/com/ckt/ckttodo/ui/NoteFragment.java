package com.ckt.ckttodo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import com.ckt.ckttodo.database.DatebaseHelper;
import com.ckt.ckttodo.database.Note;
import com.ckt.ckttodo.databinding.FragmentNoteBinding;
import com.ckt.ckttodo.databinding.NoteItemBinding;

import io.realm.RealmResults;

public class NoteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rv_note;
    private RealmResults<Note> baseList;
    private FragmentNoteBinding mFragmentNoteBinding;
    private NoteAdapter noteAdapter;
    private static Context mContext;

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
        return init(inflater);
    }

    private View init(LayoutInflater inflater) {
        mFragmentNoteBinding = FragmentNoteBinding.inflate(inflater);
        rv_note = mFragmentNoteBinding.rvNote;
        baseList = DatebaseHelper.getInstance(getContext()).findAll(Note.class);
        noteAdapter = new NoteAdapter(baseList);
        mContext = getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_note.setLayoutManager(layoutManager);
        rv_note.setAdapter(noteAdapter);
        return mFragmentNoteBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initOperation();
    }

    private void initOperation() {
        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(mContext, NewNoteActivity.class);
                intent.putExtra("noteTag", "1");
                intent.putExtra("noteGet", position);
                startActivityForResult(intent, 1);
            }
        });
        noteAdapter.setOnItemLongClickListener(new NoteAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final int position, View view) {
                new AlertDialog.Builder(mContext).setTitle("确认删除吗？").setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作 
                        DatebaseHelper.getInstance(mContext).delete(baseList.get(position));
                        noteAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作 
                    }
                }).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        baseList = DatebaseHelper.getInstance(getContext()).findAll(Note.class);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }

    public static class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> implements View.OnClickListener, View.OnLongClickListener {
        RealmResults<Note> noteList;

        public NoteAdapter(RealmResults<Note> noteList) {
            this.noteList = noteList;
        }

        @Override
        public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            NoteItemBinding noteItemBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.note_item, parent, false);
            noteItemBinding.getRoot().setOnClickListener(this);
            noteItemBinding.getRoot().setOnLongClickListener(this);
            return new NoteViewHolder(noteItemBinding);
        }

        @Override
        public void onBindViewHolder(NoteViewHolder holder, int position) {
            holder.itemView.setTag(position);
            holder.setData(noteList.get(position));
        }

        @Override
        public int getItemCount() {
            return (noteList == null) ? 0 : noteList.size();
        }

        private OnItemLongClickListener onItemLongClickListener;
        public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
            this.onItemLongClickListener = onItemLongClickListener;
        }

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public static interface OnItemClickListener {
            void onItemClick(int position, View view);
        }

        public static interface OnItemLongClickListener {
            void onItemLongClick(int position, View view);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick((Integer) v.getTag(), v);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick((Integer) v.getTag(), v);
            }
            return false;
        }

    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_noteTitle;
        public TextView tv_noteContent;
        private NoteItemBinding noteItemBinding;

        public NoteViewHolder(NoteItemBinding noteItemBinding) {
            super(noteItemBinding.getRoot());
            this.noteItemBinding = noteItemBinding;
            tv_noteTitle = noteItemBinding.tvNoteTitle;
            tv_noteContent = noteItemBinding.tvNoteContent;
        }

        public void setData(Note data) {
            noteItemBinding.setNote(data);
            noteItemBinding.executePendingBindings();
        }
    }
}
