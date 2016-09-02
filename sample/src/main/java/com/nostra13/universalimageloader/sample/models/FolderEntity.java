package com.nostra13.universalimageloader.sample.models;

/**
 * @author mochangsheng
 * @version 1.0
 * @title 类的名称
 * @description 该类的主要功能描述
 * @created 2016/7/17
 * @changeRecord [修改记录] <br/>
 */
public class FolderEntity {

    private  String mName;
    private  String mPath;
    private  int mFileCounts;
    private  int mFolderCounts;
    private  String mTotalSize = "";
    private  long mAlbumId;
    private  boolean mIsRootFolder = false;

    public FolderEntity() {
        this.mName = "";
        this.mPath = "";
        this.mFileCounts = 0;
        this.mFolderCounts = 0;
    }

    public FolderEntity(String name, String path, int songCounts) {
        this.mName = name;
        this.mPath = path;
        this.mFileCounts = songCounts;
        this.mFolderCounts = 0;
    }

    public FolderEntity(String name, String path, int songCounts, int folderCounts) {
        this.mName = name;
        this.mPath = path;
        this.mFileCounts = songCounts;
        this.mFolderCounts = folderCounts;
    }

    public FolderEntity(String name, String path, int songCounts, int folderCounts, String totalSize) {
        this.mName = name;
        this.mPath = path;
        this.mFileCounts = songCounts;
        this.mFolderCounts = folderCounts;
        this.mTotalSize = totalSize;
    }

    public FolderEntity(String name, String path, int songCounts, int folderCounts, String totalSize, long id) {
        this.mName = name;
        this.mPath = path;
        this.mFileCounts = songCounts;
        this.mFolderCounts = folderCounts;
        this.mTotalSize = totalSize;
        this.mAlbumId = id;
    }

    public FolderEntity(String name, String path, int songCounts, int folderCounts, String totalSize, long id, boolean isRootFolder) {
        this.mName = name;
        this.mPath = path;
        this.mFileCounts = songCounts;
        this.mFolderCounts = folderCounts;
        this.mTotalSize = totalSize;
        this.mAlbumId = id;
        this.mIsRootFolder = isRootFolder;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getPath() {
        return mPath;
    }

    public void setFileCounts(int counts) {
        mFileCounts = counts;
    }

    public int getFileCounts() {
        return mFileCounts;
    }

    public void setFolderCounts(int counts) {
        mFolderCounts = counts;
    }

    public int getFolderCounts() {
        return mFolderCounts;
    }

    public void setTotalSize(String size) {
        mTotalSize = size;
    }

    public String getTotalSize() {
        return mTotalSize;
    }

    public void setAlbumId(long id) {
        mAlbumId = id;
    }

    public long getAlbumId() {
        return mAlbumId;
    }

    public boolean isRootFolder() {
        return mIsRootFolder;
    }

    public void setIsRootFolder(boolean isRootFolder) {
        mIsRootFolder = isRootFolder;
    }

}
