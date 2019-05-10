package com.example.projectthree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectthree.R;
import com.example.projectthree.bean.NineGridItem;
import com.example.projectthree.bean.Picinfo;
import com.example.projectthree.layout.NineGridTestLayout;
import com.example.projectthree.widget.RecyclerExtras;


import org.w3c.dom.Text;

import java.util.List;

public class NineGridAdapter extends RecyclerView.Adapter<NineGridAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener{

    private int CLICK = 0; // 正常点击
    private int DELETE = 1; // 点击了删除按钮
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

        NineGridItem item=mList.get(position);
        holder.tv_uid.setText(item.getUid());
        holder.tv_text.setText(item.getText());
        holder.tv_location.setText(item.getLocation());
        holder.tv_time.setText(item.getTime());

        holder.tv_delete.setVisibility((item.bPressed) ? View.VISIBLE : View.GONE);
        holder.tv_delete.setId(item.id * 10 + DELETE);
        holder.ll_item.setId(item.id * 10 + CLICK);
        holder.tv_delete.setOnClickListener(this);
        // 列表项的点击事件需要自己实现
        holder.ll_item.setOnClickListener(this);
        // 列表项的长按事件需要自己实现
        holder.ll_item.setOnLongClickListener(this);


    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        NineGridTestLayout layout;
        public TextView tv_uid;
        public TextView tv_delete;
        public TextView tv_location;
        public TextView tv_text;
        public TextView tv_time;
        public LinearLayout ll_item;


        public ViewHolder(View itemView) {
            super(itemView);
            layout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);
            ll_item=itemView.findViewById(R.id.ll_item);
            tv_uid=itemView.findViewById(R.id.tv_uid);
            tv_delete=itemView.findViewById(R.id.tv_delete);
            tv_text=itemView.findViewById(R.id.tv_text);
            tv_location=itemView.findViewById(R.id.tv_location);
            tv_time=itemView.findViewById(R.id.tv_time);

        }
    }

    private int getListSize(List<NineGridItem> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    @Override
    public int getItemCount() {
        return getListSize(mList);
    }

    //根据列表项编号获得当前位置序号
    private int getPosition(int item_id){
        int pos=0;
        for(int i=0;i<mList.size();i++){
            if(mList.get(i).id==item_id){
                pos=i;
                break;
            }
        }
        return pos;
    }

    @Override
    public void onClick(View v) {
        int position = getPosition((int) v.getId() / 10);
        int type = (int) v.getId() % 10;
        if (type == CLICK) { // 正常点击，则触发点击监听器的onItemClick方法
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, position);
            }
        } else if (type == DELETE) { // 点击了删除按钮，则触发删除监听器的onItemDeleteClick方法
            if (mOnItemDeleteClickListener != null) {
                mOnItemDeleteClickListener.onItemDeleteClick(v, position);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int position = getPosition((int) v.getId() / 10);
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(v, position);
        }
        return true;
    }


    // 声明列表项的点击监听器对象
    private RecyclerExtras.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(RecyclerExtras.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // 声明列表项的长按监听器对象
    private RecyclerExtras.OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(RecyclerExtras.OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    // 声明列表项的删除监听器对象
    private RecyclerExtras.OnItemDeleteClickListener mOnItemDeleteClickListener;

    public void setOnItemDeleteClickListener(RecyclerExtras.OnItemDeleteClickListener listener) {
        this.mOnItemDeleteClickListener = listener;
    }
}
