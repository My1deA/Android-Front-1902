package com.example.projectthree.useless;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectthree.R;
import com.example.projectthree.bean.Picinfo;
import com.example.projectthree.widget.RecyclerExtras.OnItemClickListener;
import com.example.projectthree.widget.RecyclerExtras.OnItemDeleteClickListener;
import com.example.projectthree.widget.RecyclerExtras.OnItemLongClickListener;
import java.util.ArrayList;


public class MyLinenarDynamicAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private final static String TAG="LinearDynamicAdapter";
    private Context mContext;
    private ArrayList<Picinfo> mPublicArray;

    public MyLinenarDynamicAdapter(Context context, ArrayList<Picinfo> publicArray) {
        mContext = context;
        mPublicArray = publicArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_linear,viewGroup,false);
        return new ItemHolder(view);
    }

    //根据列表项编号获得当前位置序号
    private int getPosition(int item_id){
        int pos=0;
        for(int i=0;i<mPublicArray.size();i++){
            if(mPublicArray.get(i).id==item_id){
                pos=i;
                break;
            }
        }
        return pos;
    }

    private int CLICK = 0; // 正常点击
    private int DELETE = 1; // 点击了删除按钮


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picinfo pic=mPublicArray.get(i);
        ItemHolder holder=(ItemHolder)viewHolder;
        holder.tv_username.setText(pic.uid);
        holder.tv_location.setText(pic.location);
        holder.tv_desc.setText(pic.desc);
        Glide.with(mContext).load(pic.url).into(holder.iv_picture);

        holder.tv_delete.setVisibility((pic.bPressed) ? View.VISIBLE : View.GONE);
        holder.tv_delete.setId(pic.id * 10 + DELETE);
        holder.ll_item.setId(pic.id * 10 + CLICK);
        holder.tv_delete.setOnClickListener(this);
        // 列表项的点击事件需要自己实现
        holder.ll_item.setOnClickListener(this);
        // 列表项的长按事件需要自己实现
        holder.ll_item.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mPublicArray.size();
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


    // 获取列表项的类型
    public int getItemViewType(int position) {
        return 0;
    }

    // 获取列表项的编号
    public long getItemId(int position) {
        return position;
    }

    public class ItemHolder extends ViewHolder{

        public LinearLayout ll_item;
        public TextView tv_username;
        public TextView tv_desc;
        public ImageView iv_picture;
        public TextView tv_location;
        public TextView tv_delete;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ll_item=itemView.findViewById(R.id.ll_item);
            tv_username=itemView.findViewById(R.id.tv_uid);
            tv_desc=itemView.findViewById(R.id.tv_desc);
            iv_picture=itemView.findViewById(R.id.iv_picture);
            tv_location=itemView.findViewById(R.id.tv_location);
            tv_delete=itemView.findViewById(R.id.tv_delete);
        }
    }

    // 声明列表项的点击监听器对象
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // 声明列表项的长按监听器对象
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    // 声明列表项的删除监听器对象
    private OnItemDeleteClickListener mOnItemDeleteClickListener;

    public void setOnItemDeleteClickListener(OnItemDeleteClickListener listener) {
        this.mOnItemDeleteClickListener = listener;
    }
}
