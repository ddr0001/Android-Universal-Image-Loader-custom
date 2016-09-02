package com.nostra13.universalimageloader.sample.models;

import java.util.List;

/**
 * @author mochangsheng
 * @version 1.0
 * @title 类的名称
 * @description 该类的主要功能描述
 * @created 2016/8/30
 * @changeRecord [修改记录] <br/>
 */
public class FolderBackNodeInfo {

    //先前目录Item所在的Adapter中的位置
    private int mPreDirPosition;

    //先前的目录路径
    private String mPreDirPath;

    private List<FolderEntity> mPreDirList;

    public FolderBackNodeInfo(int position, String preDirPath, List<FolderEntity> list) {
        mPreDirPosition = position;
        mPreDirPath = preDirPath;
        mPreDirList = list;
    }

    /**
     * 返回先前的目录路径
     * @return
     */
    public String getPreDirPath() {
        return mPreDirPath;
    }

    /**
     * 设置先前的目录路径
     * @param preDirPath
     */
    public void setPreDirPath(String preDirPath) {
        mPreDirPath = preDirPath;
    }


    /**
     * 设置先前目录Item所在的Adapter中的位置
     * @param preDirPosition
     */
    public void setPreDirPosition(int preDirPosition) {
        mPreDirPosition = preDirPosition;
    }

    /**
     * 获取先前目录Item所在的Adapter中的位置
     * @return
     */
    public int getPreDirPosition() {
        return mPreDirPosition;
    }

    public List<FolderEntity> getPreDirList() {
        return mPreDirList;
    }

    public void setPreDirList(List<FolderEntity> list) {
        mPreDirList = list;
    }
 }
