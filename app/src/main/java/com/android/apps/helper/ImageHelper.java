package com.android.apps.helper;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by root on 17/5/3.
 */

public class ImageHelper {
    private static ImageHelper imageHelper;
    private Context mContext;

    private ImageHelper(){

    }

    private ImageHelper(Context context){
        this.mContext = context.getApplicationContext();
    }

    public static ImageHelper getInstance(Context context){
        if(imageHelper == null){
            synchronized (ImageHelper.class){
                if(imageHelper == null){
                    imageHelper = new ImageHelper(context);
                }
            }
        }
        return imageHelper;
    }

    /**
     * 加载网络图片
     * @param imgUrl
     * @param mImg
     */
    public void loadImage(String imgUrl, ImageView mImg){
        Glide.with(mContext).load(imgUrl).into(mImg);
    }

    /**
     * 加载网络图片
     * @param imgUrl
     * @param mImg
     * @param placeImg
     */
    public void loadImage(String imgUrl, ImageView mImg, int placeImg){
        Glide.with(mContext).load(imgUrl).placeholder(placeImg).into(mImg);
    }

    /**
     * 加载网络图片
     * @param imgUrl
     * @param mImg
     * @param placeImg
     * @param errorImg
     */
    public void loadImage(String imgUrl, ImageView mImg, int placeImg, int errorImg){
        Glide.with(mContext).load(imgUrl).placeholder(placeImg).error(errorImg).into(mImg);
    }

    /**
     * 加载网络图片
     * @param imgUrl
     * @param mImg
     * @param placeImg
     * @param errorImg
     */
    public void loadGifImage(String imgUrl, ImageView mImg, int placeImg, int errorImg){
        Glide.with(mContext).load(imgUrl).asGif().placeholder(placeImg).error(errorImg).into(mImg);
    }

    /**
     * 加载本地图片
     * @param resId
     * @param mImg
     */
    public void loadLocalImage(int resId, ImageView mImg){
        Glide.with(mContext).load(resId).into(mImg);
    }

    /**
     * 加载本地图片
     * @param imgPath
     * @param mImg
     */
    public void loadLocalImage(String imgPath, ImageView mImg){
        File file = new File(imgPath);
        if(file.exists())
            Glide.with(mContext).load(file).into(mImg);
    }


    /**
     * 加载本地图片
     * @param imgPath
     * @param mImg
     * @param placeImg
     */
    public void loadLocalImage(String imgPath, ImageView mImg, int placeImg){
        File file = new File(imgPath);
        if(file.exists())
            Glide.with(mContext).load(file).placeholder(placeImg).into(mImg);
    }


    /**
     * 加载本地图片
     * @param imgPath
     * @param mImg
     * @param placeImg
     * @param errorImg
     */
    public void loadLocalImage(String imgPath, ImageView mImg, int placeImg, int errorImg){
        File file = new File(imgPath);
        if(file.exists())
            Glide.with(mContext).load(file).placeholder(placeImg).error(errorImg).into(mImg);
    }
}
