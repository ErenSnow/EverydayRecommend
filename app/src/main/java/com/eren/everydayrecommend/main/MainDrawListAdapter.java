package com.eren.everydayrecommend.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.home.model.DrawModel;

import java.util.List;

/**
 * 作者：Leon
 * 描述:
 */
public class MainDrawListAdapter extends RecyclerView.Adapter<MainDrawListAdapter.MainViewHolder> {

    private Context mContex;
    private List<DrawModel> mList;
    OnMainDrawClickListener mOnMainDrawClickListener;

    interface OnMainDrawClickListener {
        void onClick(int position);
    }

    public MainDrawListAdapter(Context mContex, List<DrawModel> mList) {
        this.mContex = mContex;
        this.mList = mList;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContex, R.layout.item_main_drawerlist, null);
        MainViewHolder mainViewHolder = new MainViewHolder(view);
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        holder.imageView.setBackgroundResource(mList.get(position).getResourceID());
        holder.textView.setText(mList.get(position).getTitle());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMainDrawClickListener != null) {
                    mOnMainDrawClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setmList(List<DrawModel> mList) {
        this.mList = mList;
    }

    public void setmOnMainDrawClickListener(OnMainDrawClickListener mOnMainDrawClickListener) {
        this.mOnMainDrawClickListener = mOnMainDrawClickListener;
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        ImageView imageView;
        TextView textView;

        public MainViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_main);
            imageView = (ImageView) itemView.findViewById(R.id.iv_icon);
            textView = (TextView) itemView.findViewById(R.id.tv_info);
        }
    }
}
