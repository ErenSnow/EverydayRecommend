package com.eren.everydayrecommend.read;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.common.Utils;
import com.eren.everydayrecommend.detail.DetailActivity;
import com.eren.everydayrecommend.image.ImageManager;
import com.eren.everydayrecommend.read.model.ReadModel;

import java.util.List;

/**
 * Read适配器
 */
public class ReadAdapter extends RecyclerView.Adapter<ReadAdapter.ReadViewHolder> {

    private Context mContext;
    private List<ReadModel.NewslistEntity> mList;


    public ReadAdapter(Context mContex, List<ReadModel.NewslistEntity> mList) {
        this.mContext = mContex;
        this.mList = mList;
    }

    public void setmList(List<ReadModel.NewslistEntity> mList) {
        this.mList = mList;
    }

    @Override
    public ReadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment, parent, false);
        ReadViewHolder viewHolder = new ReadViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReadViewHolder holder, int position) {
        final ReadModel.NewslistEntity NewslistBean = mList.get(position);
        holder.tvTitle.setText(NewslistBean.getTitle());
        holder.tvTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Utils.formatDateFromStr(NewslistBean.getCtime())));
        holder.tvAuthor.setText(NewslistBean.getDescription());
        ImageManager.getInstance()
                .loadImage(mContext, NewslistBean.getPicUrl(), holder.ivCover);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("url", NewslistBean.getUrl());
                intent.putExtra("isfromme", true);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ReadViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvTime;
        ImageView ivCover;//封面缩率图

        public ReadViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
        }
    }
}
