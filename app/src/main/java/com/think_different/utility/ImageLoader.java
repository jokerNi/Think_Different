package com.think_different.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by oceancx on 15/3/7.
 */
public class ImageLoader  {

    /**
     * 自定义的imageloader 单例
     */
    private LruCache<String, Bitmap> mImageCache;

    private static ImageLoader mImageLoader;

    // 非线程安全
    public static ImageLoader getInstance() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader();
        }
        return mImageLoader;
    }

    public ImageLoader() {
        int maxSize = (int) Runtime.getRuntime().maxMemory() / 1024;
        mImageCache = new LruCache<String, Bitmap>(maxSize / 8) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void addToImageCache(String key, Bitmap value) {
        if (mImageCache.get(key) == null)
            mImageCache.put(key, value);
    }

    public Bitmap getBitmapFromCache(String key) {
        return mImageCache.get(key);
    }

    /**
     * 还是对宽度进行压缩吧 这样好看一点????
     *
     * @param filepath
     * @param reqHeight
     * @return
     */
    public Bitmap decodeBitmapFromFile(String filepath, int reqWidth,
                                       int reqHeight) {
        /**
         * 从bitmap中解析图片
         */

        File f = new File(filepath);

        if (!f.exists()) {
            DebugLog.e(" do i exist");
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(fis, null, options);
        options.inJustDecodeBounds=false;

        if (options.outWidth > reqWidth) {
            options.inSampleSize = Math.round((float)options.outWidth
                    / (float)reqWidth);
        }
        if (options.outHeight > reqHeight) {
            options.inSampleSize = options.inSampleSize < Math
                    .round((float)options.outHeight / (float)reqHeight) ? options.inSampleSize
                    : Math.round((float)options.outHeight / (float)reqHeight);
        }


        try {
            fis.close();
            fis=new FileInputStream(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(fis, null, options);
    }

//    public Bitmap FindImageInSDcard(String url,int reqWidth,int reqHeight){
//        String filename= AppVars.UrlToFileName(url);
//        File f=new File(AppVars.FImageDir()+filename);
//        if(! f.exists())return null;
//        /**
//         * 从file中解析出bitmap
//         *
//         * reqWidth 暂时算作屏幕宽度-68dp
//         * 高度就是140dp
//         */
//        return decodeBitmapFromFile(AppVars.FImageDir()+filename,reqWidth, reqHeight );
//    }
//    public Bitmap FindImageInSDcardByFile(File f,int reqWidth,int reqHeight){
//        /**
//         * 从file中解析出bitmap
//         *
//         * reqWidth 暂时算作屏幕宽度-68dp
//         * 高度就是140dp
//         */
//
//        return decodeBitmapFromFile(AppVars.FImageDir()+f.getName(),reqWidth, reqHeight );
//    }


}
