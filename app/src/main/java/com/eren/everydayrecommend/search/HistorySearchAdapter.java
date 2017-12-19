package com.eren.everydayrecommend.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eren.everydayrecommend.R;

import java.util.List;

/**
 * @author Leon
 * @date
 */

public class HistorySearchAdapter extends RecyclerView.Adapter<HistorySearchAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mList;
    OnClickListener mOnClickListener;

    interface OnClickListener{
        //条目单击事件
        void onClick(int position);
        //清除按钮单击事件
        void onClearClick(int position);
    }


    public HistorySearchAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_history_search, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(mList.get(position));
        //清除按钮点击事件
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onClearClick(position);
                notifyDataSetChanged();
            }
        });
        //整个条目点击事件
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
    }

    public void setmOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        RelativeLayout relativeLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_info);
            imageView = (ImageView) itemView.findViewById(R.id.iv_clear);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_historysearch);
        }
    }
}
