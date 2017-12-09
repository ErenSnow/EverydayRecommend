package com.eren.everydayrecommend.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.target.Target;
import com.eren.everydayrecommend.R;


/**
 * Glide图片处理类
 */
public class ImageManager {
    private static ImageManager mImageManager = null;

    private ImageManager() {
    }

    public static ImageManager getInstance() {
        if (mImageManager == null) {
            mImageManager = new ImageManager();
        }
        return mImageManager;
    }

    /**
     * 加载图片
     *
     * @param context
     * @param path
     * @param targetImageView 使用默认的占位等待图片和错误图片
     */
    public void loadImage(Context context, Object path, ImageView targetImageView) {
        Glide.with(context).load(path)
                .skipMemoryCache(false)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.notfound)
                .into(targetImageView);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param path
     * @param targetImageView 使用默认的占位等待图片和错误图片
     * @param transformation  图片转换器
     */
    public void loadImage(Context context, Object path, ImageView targetImageView, Transformation transformation) {
        Glide.with(context)
                .load(path)
                .bitmapTransform(transformation)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(targetImageView);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param path
     * @param targetImageView
     * @param placeHolderResourceId 加载中占位图片
     * @param errorResourceId       加载错误占位图片
     */
    public void loadImage(Context context, Object path, ImageView targetImageView, int placeHolderResourceId, int errorResourceId) {
        Glide.with(context)
                .load(path)
                .placeholder(placeHolderResourceId)
                .error(errorResourceId)
                .into(targetImageView);
    }

    /**
     * 获取图片bitmap
     *
     * @param context
     * @param path
     * @return
     */
    public Bitmap loadImage(Context context, Object path) {
        Bitmap bitmap;
        try {
            bitmap = Glide.with(context)
                    .load(path)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

}
