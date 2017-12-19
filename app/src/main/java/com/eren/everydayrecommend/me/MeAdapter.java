package com.eren.everydayrecommend.me;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.detail.DetailActivity;
import com.eren.everydayrecommend.image.ImageManager;
import com.eren.everydayrecommend.me.model.VideoModel;

import java.util.List;

/**
 * @author Leon
 * @date
 */

public class MeAdapter extends RecyclerView.Adapter<MeAdapter.MeViewHolder> {
    private Context mContext;
    private List<VideoModel> mList;

    public MeAdapter(Context mContext, List<VideoModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public MeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_me, null);
        return new MeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeViewHolder holder, final int position) {
        ImageManager.getInstance()
                .loadImage(mContext, mList.get(position).getResourceId(), holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("isfromme", true);
                intent.putExtra("url", mList.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MeViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_me_iv_video);
        }
    }
}
