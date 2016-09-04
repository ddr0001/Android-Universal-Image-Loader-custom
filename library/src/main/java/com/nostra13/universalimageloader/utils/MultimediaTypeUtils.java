package com.nostra13.universalimageloader.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mochangsheng
 * @version 1.0
 * @title MultimediaTypeUtils
 * @description 处理缩略图类型的工具
 * @created 2016/9/4 0004
 * @changeRecord [修改记录] <br/>
 */
public class MultimediaTypeUtils {

    public static Map<String, MultimediaType> sExtMap;

    /**
     * 文件类型
     */
    public static enum MultimediaType {

        //无效文件
        MMT_INVALID("非文件"),

        //图片
        MMT_IMAGE("图片"),

        //音频
        MMT_MUSIC("音频"),

        //视频
        MMT_VIDEO("视频");

        private String str;

        private MultimediaType(String str) {
            this.str = str;
        }

        public String toString() {
            return this.str;
        }
    }

    /**
     * 支持的后缀名类型
     */
    // 图片
    private static String[] sExtImage = {
            ".png",
            ".jpg",
            ".bmp",
            ".jpeg",
            ".gif",
            ".jpe",
            ".tif",
            ".tiff",
    };

    // 音乐
    private static String[] sExtAudio = {
            ".mp3",
            ".mp2",
            ".wav",
            ".midi",
            ".m4a",
            ".wma",
            ".acc",
            ".aac",
            ".ogg",
            ".amr",
            ".flac",
            ".ape ",
    };

    // 电影
    private static String[] sExtVideo = {
            ".mp4",
            ".rmvb",
            ".3gp",
            ".flv",
            ".avi",
            ".dat",
            ".f4ts",
            ".f4v",
            ".m2ts",
            ".mkv",
            ".mov",
            ".mpeg",
            ".mpg",
            ".trp",
            ".ts",
            ".tp",
            ".v01",
            ".m2ts",
            ".von",
            ".rm",
            ".wmv",
            ".tp",
            ".mvc",
            ".iso",
            ".vob",
            ".asf",
            ".m2t",
    };

    static {

        sExtMap = new HashMap<String, MultimediaType>();
        //初始化后缀名Map
        for (String ext : sExtAudio) {
            sExtMap.put(ext, MultimediaType.MMT_MUSIC);
        }
        for (String ext : sExtImage) {
            sExtMap.put(ext, MultimediaType.MMT_IMAGE);
        }
        for (String ext : sExtVideo) {
            sExtMap.put(ext, MultimediaType.MMT_VIDEO);
        }
    }


    /**
     * 获取文件类型
     * @param path
     * @return
     */
    public static MultimediaType getMultimediaType(String path) {
        int index = path.lastIndexOf('.');
        if (index == -1)
            return MultimediaType.MMT_INVALID;
        String postfix = path.substring(index).toLowerCase();
        if (sExtMap.containsKey(postfix)) {
            return sExtMap.get(postfix);
        }
        return MultimediaType.MMT_INVALID;
    }
}
