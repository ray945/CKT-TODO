package com.ckt.ckttodo.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ckt.ckttodo.database.Project;

import java.util.List;

/**
 * Created by mozre on 9/10/17.
 */
public class ProjectAdapter extends BaseQuickAdapter<Project,BaseViewHolder> {


    public ProjectAdapter(@LayoutRes int layoutResId, @Nullable List<Project> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Project item) {

    }
}
