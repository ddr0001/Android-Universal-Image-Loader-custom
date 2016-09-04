package com.nostra13.universalimageloader.core.download;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.MultimediaTypeUtils;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author mochangsheng
 * @version 1.0
 * @title CustomImageDownLoader
 * @description 自定义ImageDownLoader类，支持图片缩略图、音乐缩略图、视频缩略图
 * @created 2016/9/4 0004
 * @changeRecord [修改记录] <br/>
 */
public class CustomImageDownLoader extends BaseImageDownloader {

    private static final String TAG = CustomImageDownLoader.class.getSimpleName();

    public CustomImageDownLoader(Context context) {
        super(context);
    }

    public CustomImageDownLoader(Context context, int connectTimeout, int readTimeout) {
        super(context, connectTimeout, readTimeout);
    }

    @Override
    protected InputStream getStreamFromFile(String imageUri, Object extra) throws IOException {
        L.d("========" + TAG + " getStreamFromFile imageUri==%s", imageUri);
        String filePath = Scheme.FILE.crop(imageUri);
        if (isVideoFileUri(imageUri)) {
            return getVideoThumbnailStream(filePath);//本地视频文件
        } else if (isMusicFileUri(imageUri)) {
            return getMusicThumbnailStream(filePath);//本地音乐文件
        } else {
            BufferedInputStream imageStream = new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE);
            return new ContentLengthInputStream(imageStream, (int) new File(filePath).length());
        }
    }

    private boolean isVideoFileUri(String uri) {
        if (MultimediaTypeUtils.getMultimediaType(uri)
                .equals(MultimediaTypeUtils.MultimediaType.MMT_VIDEO)) {
            return true;
        }

        return false;
    }

    private boolean isMusicFileUri(String uri) {
        if (MultimediaTypeUtils.getMultimediaType(uri)
                .equals(MultimediaTypeUtils.MultimediaType.MMT_MUSIC)) {
            return true;
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private InputStream getVideoThumbnailStream(String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            Bitmap bitmap = ThumbnailUtils
                    .createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
            if (bitmap != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                return new ByteArrayInputStream(bos.toByteArray());
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    private InputStream getMusicThumbnailStream(String filePath) {
        L.d("========" + TAG + " getMusicThumbnailStream filePath==%s", filePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            Bitmap bitmap = null;
            /*MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(filePath); //设置数据源
                byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
                bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
            } catch (IllegalArgumentException ex) {
                // Assume this is a corrupt video file
            } catch (RuntimeException ex) {
                // Assume this is a corrupt video file.
            } finally {
                try {
                    retriever.release();
                } catch (RuntimeException ex) {
                    // Ignore failures while cleaning up.
                }
            }*/

            AudioFile f = null;
            try {
                f = AudioFileIO.read(new File(filePath));
                Tag tag = f.getTag();
                if (tag.getArtworkList().size() != 0) {
                    Artwork artworkFirst = tag.getArtworkList().get( 0 );
                    bitmap = BitmapFactory.decodeByteArray( artworkFirst.getBinaryData(),
                            0,
                            artworkFirst.getBinaryData().length );
                }
                L.d("========" + TAG + " getMusicThumbnailStream bitamp==%s", bitmap);
            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                return new ByteArrayInputStream(bos.toByteArray());
            }
        }

        return null;
    }

}
