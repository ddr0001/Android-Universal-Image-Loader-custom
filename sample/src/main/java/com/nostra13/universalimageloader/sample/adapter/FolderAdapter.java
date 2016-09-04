package com.nostra13.universalimageloader.sample.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.mcs.library.adapter.recyclerView.CommonAdapter;
import com.mcs.library.adapter.recyclerView.base.ViewHolder;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.CustomImageDownLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.models.FolderEntity;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mochangsheng
 * @version 1.0
 * @title 类的名称
 * @description 该类的主要功能描述
 * @created 2016/7/18
 * @changeRecord [修改记录] <br/>
 */
public class FolderAdapter extends CommonAdapter<FolderEntity> {

    private List<FolderEntity> mList;
    private DisplayImageOptions mOptions;

    public FolderAdapter(Context context, int layoutId, List<FolderEntity> datas) {
        super(context, layoutId, datas);
        mList = datas;

        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .build();


        ImageLoader.getInstance().destroy();//如果已经初始化过，就得destroy之后才能再次init成功
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.imageDownloader(new CustomImageDownLoader(mContext));
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
    protected void convert(ViewHolder holder, FolderEntity folderEntity, int position) {

        File file = new File(folderEntity.getPath());

        holder.setText(R.id.folder_name,folderEntity.getName());
        String songNumbers = "";
        String folderNumbers = "";
        String content = "";

        if (file.isDirectory()) {
            holder.setImageResource(R.id.folder_ic, R.drawable.ic_folder);
            if (folderEntity.getFolderCounts() != 0 && folderEntity.getFileCounts() != 0) {
                songNumbers = makeLabel(mContext, R.plurals.NFiles, folderEntity.getFileCounts());
                folderNumbers = makeLabel(mContext, R.plurals.NFolders, folderEntity.getFolderCounts());
                content = makeCombinedStringWithComma(mContext, folderNumbers, songNumbers);
            } else if (folderEntity.getFolderCounts() == 0 && folderEntity.getFileCounts() == 0) {
                content = "空文件夹";
            } else if (folderEntity.getFileCounts() == 0) {
                folderNumbers = makeLabel(mContext, R.plurals.NFolders, folderEntity.getFolderCounts());
                content = folderNumbers;
            } else if (folderEntity.getFolderCounts() == 0) {
                songNumbers = makeLabel(mContext, R.plurals.NFiles, folderEntity.getFileCounts());
                content = songNumbers;
            }
        } else {
            //holder.setImageResource(R.id.folder_ic, R.drawable.ic_launcher);
            String uri = "file://" + folderEntity.getPath();
            ImageLoader.getInstance().displayImage
                    (uri, (ImageView) holder.getView(R.id.folder_ic), mOptions, mImageLoaderListener);

            content = folderEntity.getTotalSize();
            //loadAlbum(folderEntity, holder);
        }

        holder.setText(R.id.folder_content, content);

        /*Trace.Debug("convert name==" + folderEntity.getName() + "; "
                + content);*/
    }

    /*private void loadAlbum(FolderEntity folderEntity, ViewHolder holder) {
        ImageLoader.getInstance().displayImage(
                Utils.getAlbumArtUri(folderEntity.getAlbumId()).toString(),
                (ImageView) holder.getView(R.id.folder_ic),
                new DisplayImageOptions.Builder().cacheInMemory(true).
                        showImageOnFail(R.mipmap.ic_launcher).
                        resetViewBeforeLoading(true).build());
    }*/

    private  SimpleImageLoadingListener mImageLoaderListener =  new SimpleImageLoadingListener() {

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
            }
        }
    };

    /**
     * 按两个String组合起来   String , String
     * @param context
     * @param first
     * @param second
     * @return
     */
    public static final String makeCombinedStringWithComma(final Context context, final String first,
                                                           final String second) {
        final String formatter = context.getResources().getString(R.string.combine_two_strings_with_comma);
        return String.format(formatter, first, second);
    }

    /**
     * 从plurals标签中获取对应的String值
     * @param context
     * @param pluralInt
     * @param number
     * @return
     */
    public static final String makeLabel(final Context context, final int pluralInt,
                                         final int number) {
        return context.getResources().getQuantityString(pluralInt, number, number);
    }
}
