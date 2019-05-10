package com.example.projectthree.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.HashMap;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class VideoPicUtil {

    public void getVideoInfo(View view, ImageView iv_image, String path) {
        Bitmap videoThumbnail = null;

        //获取本地视频缩略图，在sdk根目录下准备一个test.mp4的文件
//        String videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4";
//         videoThumbnail = getVideoThumbnail(videoPath);

        //获取网络视频缩略图
        videoThumbnail = getNetVideoThumbnail(path);

        iv_image.setImageBitmap(videoThumbnail);

    }

    /**
     * 获取本地视频缩略图
     * @param filePath
     * @return
     */
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap b=null;
        //使用MediaMetadataRetriever
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        //FFmpegMediaMetadataRetriever
        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
        File file = new File(filePath);
        try {

            retriever.setDataSource(file.getPath());
            b=retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }


    /**
     * 获取网络视频缩略图
     * @param url
     * @return
     */
    public Bitmap getNetVideoThumbnail(String url) {
        Bitmap b=null;
        //使用MediaMetadataRetriever
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        //FFmpegMediaMetadataRetriever
        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();

        try {
            retriever.setDataSource(url,new HashMap<String, String>());
            b=retriever.getFrameAtTime(400*1000,FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }
}
