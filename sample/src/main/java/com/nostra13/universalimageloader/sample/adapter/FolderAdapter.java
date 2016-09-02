package com.nostra13.universalimageloader.sample.adapter;

import android.content.Context;

import com.mcs.library.adapter.recyclerView.CommonAdapter;
import com.mcs.library.adapter.recyclerView.base.ViewHolder;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.models.FolderEntity;

import java.io.File;
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

    public FolderAdapter(Context context, int layoutId, List<FolderEntity> datas) {
        super(context, layoutId, datas);
        mList = datas;
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
            holder.setImageResource(R.id.folder_ic, R.drawable.ic_launcher);
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
