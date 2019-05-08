package com.example.projectthree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectthree.R;
import com.example.projectthree.bean.NineGridItem;
import com.example.projectthree.layout.NineGridTestLayout;


import java.util.List;

public class NineGridAdapter extends RecyclerView.Adapter<NineGridAdapter.ViewHolder>{

    private Context mContext;
    private List<NineGridItem> mList;
    protected LayoutInflater inflater;

    public NineGridAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<NineGridItem> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.item_grid, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.layout.setIsShowAll(mList.get(position).isShowAll);
        holder.layout.setUrlList(mList.get(position).urlList);
    }

    @Override
    public int getItemCount() {
        return getListSize(mList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NineGridTestLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);
        }
    }

    private int getListSize(List<NineGridItem> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
