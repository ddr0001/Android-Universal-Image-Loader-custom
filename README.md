## Android-Universal-Image-Loader-custom ##

### 说明 ###

分支于Android-Universal-Image-Loader

（1）添加了音乐文件缩略图的获取（只适用于File://），基于Jaudiotagger库。

（2）File://类型的Uri视频缩略图：修改原来的根据MimeType判断为文件后缀名判断。

用法和Android-Universal-Image-Loader一致，都是根据后缀名来进行判断文件类型的，从而走对应的获取流。


### 接受的URIs ###
	"http://site.com/image.png" // from Web
	
	"file:///mnt/sdcard/image.png" // from SD card
	
	"file:///mnt/sdcard/video.mp4" // from SD card (video thumbnail)
	
	"file:///mnt/sdcard/video.mp3" // from SD card (music thumbnail)
	
	"content://media/external/images/media/13" // from content provider
	
	"content://media/external/video/media/13" // from content provider (video thumbnail)
	
	"assets://image.png" // from assets
	
	"drawable://" + R.drawable.img // from drawables (non-9patch images)

### 引用 ###
* [https://github.com/nostra13/Android-Universal-Image-Loader](https://github.com/nostra13/Android-Universal-Image-Loader)

* [https://github.com/hexise/jaudiotagger-android](https://github.com/hexise/jaudiotagger-android)