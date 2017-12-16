package com.eren.everydayrecommend.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.eren.everydayrecommend.R;
import com.eren.everydayrecommend.common.Constant;
import com.eren.everydayrecommend.common.Utils;
import com.eren.everydayrecommend.home.model.GankModel;
import com.eren.everydayrecommend.image.ImageManager;

import java.util.List;

/**
 * 适配器基类
 */
public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {
    private Context mContext;
    private List<GankModel.ResultsEntity> mListData;
    private OnBaseClickListener mBaseClickListener;
    private int mItemType;//条目类型

    /**
     * 监听回调接口定义
     */
    public interface OnBaseClickListener {
        void onClick(int position, GankModel.ResultsEntity entity);

        void onCoverClick(int position, GankModel.ResultsEntity entity);
    }

    public void addOnClickListener(OnBaseClickListener baseClickListener) {
        this.mBaseClickListener = baseClickListener;
    }

    public BaseAdapter(Context mContext, List<GankModel.ResultsEntity> mListData, int mItemType) {
        this.mContext = mContext;
        this.mListData = mListData;
        this.mItemType = mItemType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (mItemType == Constant.ITEM_TYPE_TEXT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment_girl, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GankModel.ResultsEntity resultsEntity = mListData.get(position);
        if (mItemType == Constant.ITEM_TYPE_TEXT) {
            holder.tvTitle.setText(resultsEntity.getDesc());
            holder.tvTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Utils.formatDateFromStr(resultsEntity.getPublishedAt())));
            holder.tvAuthor.setText(resultsEntity.getWho());
            if (resultsEntity.getImages() != null && resultsEntity.getImages().size() > 0) {
                //如果存在图片，则展示缩率图
                holder.ivCover.setVisibility(View.VISIBLE);
                ImageManager.getInstance().loadImage(mContext, resultsEntity.getImages().get(0), holder.ivCover);
            } else {
                //如果不存在图片，则不展示缩率图
                holder.ivCover.setVisibility(View.GONE);
            }
            //缩率图点击事件响应
            holder.ivCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (resultsEntity.getImages() != null && resultsEntity.getImages().size() > 0) {

                        mBaseClickListener.onCoverClick(position, resultsEntity);
                    } else {
                        ToastUtils.showShortSafe("木有发现图片哟");
                    }
                }
            });
        }
        if (mItemType == Constant.ITEM_TYPE_IMAGE) {
            //加载福利模式下图片
            ImageManager.getInstance()
                    .loadImage(mContext,
                            resultsEntity.getUrl(),
                            holder.imageViewGirl);
        }
        //列表单个条目点击事件响应
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseClickListener.onClick(position, resultsEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public void setmListData(List<GankModel.ResultsEntity> mListData) {
        this.mListData = mListData;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //整体布局
        View rootView;
        //标题
        TextView tvTitle;
        //作者
        TextView tvAuthor;
        //时间
        TextView tvTime;
        //封面缩率图
        ImageView ivCover;
        //福利模式下的图片控件
        ImageView imageViewGirl;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            imageViewGirl = (ImageView) itemView.findViewById(R.id.iv_girl);
        }
    }
}
