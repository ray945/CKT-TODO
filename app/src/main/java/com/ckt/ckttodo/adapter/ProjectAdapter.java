package com.ckt.ckttodo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.ckt.ckttodo.R;

/**
 * Created by hcy on 17-6-8.
 */

public class ProjectAdapter extends SectionedRecyclerViewAdapter<ProjectAdapter.MainVH> {

    @Override
    public int getSectionCount() {
        return 2;
    }

    @Override
    public int getItemCount(int section) {
        switch (section) {
            case 0:
                return 4;
            case 1:
                return 10;
            default:
                return 0;
        }
    }

    @Override
    public void onBindHeaderViewHolder(MainVH holder, int section, boolean expanded) {
        switch (section) {
            case 0:
                holder.title.setText("我参与的项目");
                break;
            case 1:
                holder.title.setText("我拥有的项目");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBindViewHolder(
            MainVH holder, int section, int relativePosition, int absolutePosition) {
    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    @Override
    public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.item_persoanl_project_header;
                break;
            case VIEW_TYPE_ITEM:
                layout = R.layout.item_personal_project;
                break;
            default:
                layout = R.layout.item_personal_project;
                break;
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MainVH(v, this);
    }

    static class MainVH extends SectionedViewHolder {

        final ProjectAdapter adapter;
        final TextView title;

        MainVH(View itemView, ProjectAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.title = (TextView) itemView.findViewById(R.id.header);

        }

    }
}