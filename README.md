# AndroidCameraApp
An Android Camera Project with ADT.

------

## Development Requirements ##

- JDK
- Eclipse
  * Eclipse IDE for Java Developers
- EGit
  * online install: http://download.eclipse.org/egit/updates/
- ADT
  * online install: https://dl-ssl.google.com/android/eclipse/
  * offline install: Location：[jar:file:ADT-23.0.7.zip]()
- Android SDK
  * online install: http://dl.google.com/android/android-sdk_r24.4.1-linux.tgz
  * offline install: installer_r24.4.1-windows.exe
  * [Android SDK开发包国内下载地址](http://www.cnblogs.com/bjzhanghao/archive/2012/11/14/android-platform-sdk-download-mirror.html)
- [AndroidDevTools](https://github.com/inferjay/AndroidDevTools)


## Android Sites

* [https://www.android.com/](https://www.android.com/)
* [http://www.anddev.org/](http://www.anddev.org/)
* [http://www.learn-android.com/](http://www.learn-android.com/)
* [http://www.appsrox.com/](http://www.appsrox.com/ "Learn Android Development | Download Free Apps")
* [http://www.eoeandroid.com/forum.php](http://www.eoeandroid.com/forum.php "Android开发者社区")
* [http://wear.techbrood.com/index.html](http://wear.techbrood.com/index.html "Android安卓开发官方文档 - Techbrood踏得网国内镜像站点")
* [http://www.android-studio.com.cn/](http://www.android-studio.com.cn/)
* [http://www.android100.org/](http://www.android100.org/)
* [http://www.csdn.net/article/2015-07-30/2825333](http://www.csdn.net/article/2015-07-30/2825333 "开发第一个Android应用之前你需要知道的六件事")

## 环境搭建 ##
* [http://www.ibm.com/developerworks/opensource/tutorials/os-eclipse-androidwidget/](http://www.ibm.com/developerworks/opensource/tutorials/os-eclipse-androidwidget/ "Introduction to Android development Using Eclipse and Android widgets")

## 程序调试
* [http://www.cnblogs.com/qingblog/archive/2012/07/27/2611469.html](http://www.cnblogs.com/qingblog/archive/2012/07/27/2611469.html "Android eclipse中程序调试")

## 应用打包
在Eclipse中将Android工程打包成apk。

* 生成keystore文件

    keytool -genkey -alias Camera -keyalg RSA -validity 100000 -keystore AndroidCameraApp.keystore

* 签名Apk文件

（1）在Eclipse中右键单击项目名称，选择"Android Tools"，再选择"Export Signed Application Package…"；  
（2）使用第三方工具：**爱加密签名工具**；  
（3）使用命令行：  
```
    jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore E:\AndroidCameraApp.keystore -storepass 123456 -keypass 123456 -signedjar E:\signed.apk E:\unsign.apk Camera
```

## 应用认领
应用认领那些事：   
[http://droidyue.com/blog/2014/12/14/android-yingyong-renling/?utm_source=tuicool&utm_medium=referral](http://droidyue.com/blog/2014/12/14/android-yingyong-renling/?utm_source=tuicool&utm_medium=referral)

## OpenCV for Android
[http://opencv.org/platforms/android.html](http://opencv.org/platforms/android.html "OpenCV for Android")

opencv for android 教程（环境搭建篇）：
[http://blog.csdn.net/pwh0996/article/details/8957764](http://blog.csdn.net/pwh0996/article/details/8957764)

## Android Camera
* [http://opencamera.org.uk/](http://opencamera.org.uk/ "Open Camera")
* [http://blog.rhesoft.com/2015/04/02/tutorial-how-to-use-camera-with-android-and-android-studio/](http://blog.rhesoft.com/2015/04/02/tutorial-how-to-use-camera-with-android-and-android-studio/ "How to use camera with Android and Android Studio")
* [http://junjunguo.com/articles/android-take-photo-show-in-list-view/](http://junjunguo.com/articles/android-take-photo-show-in-list-view/ "Android take photo and save to gallery, choose photo from gallery and show in ListView")  

### Issues
* [http://stackoverflow.com/questions/8744994/android-camera-set-resolution](http://stackoverflow.com/questions/8744994/android-camera-set-resolution "Android Camera Set Resolution")
* [http://stackoverflow.com/questions/10913181/camera-preview-is-not-restarting](http://stackoverflow.com/questions/10913181/camera-preview-is-not-restarting "camera preview is not restarting?")
* [http://stackoverflow.com/questions/10913682/how-to-capture-and-save-an-image-using-custom-camera-in-android](http://stackoverflow.com/questions/10913682/how-to-capture-and-save-an-image-using-custom-camera-in-android "How to capture and save an image using custom camera in Android?")
* [http://stackoverflow.com/questions/11121963/how-can-i-set-camera-preview-size-to-squared-aspect-ratio-in-a-squared-surfacevi](http://stackoverflow.com/questions/11121963/how-can-i-set-camera-preview-size-to-squared-aspect-ratio-in-a-squared-surfacevi "How can I set camera preview size to squared aspect ratio in a squared SurfaceView (like Instagram)")

## Gyroscope
* GyroscopeExplorer App: [https://github.com/KEOpenSource/GyroscopeExplorer](https://github.com/KEOpenSource/GyroscopeExplorer)
* [http://www.41post.com/3745/programming/android-acessing-the-gyroscope-sensor-for-simple-applications](http://www.41post.com/3745/programming/android-acessing-the-gyroscope-sensor-for-simple-applications "Android: Acessing the gyroscope sensor for simple applications")

## DateTimePicker Control
* [http://blog.csdn.net/u012246458/article/details/49800271](http://blog.csdn.net/u012246458/article/details/49800271 "日期选择器 - Android自定义DataTimePicker以及日期范围限制")
* [http://blog.csdn.net/csdnadcode/article/details/39555519](http://blog.csdn.net/csdnadcode/article/details/39555519 "Android DatePicker 限制日期选择范围")
* [http://blog.csdn.net/njweiyukun/article/details/50338183](http://blog.csdn.net/njweiyukun/article/details/50338183 "Android中DatePicker只显示年月的方法")

## 自定义对话框
* [http://www.cnblogs.com/weixing/archive/2013/08/14/3257077.html](http://www.cnblogs.com/weixing/archive/2013/08/14/3257077.html "Android自定义对话框(Dialog)位置,大小")
* [http://blog.csdn.net/fancylovejava/article/details/21617553](http://blog.csdn.net/fancylovejava/article/details/21617553 "Android自定义对话框(Dialog)位置,大小")

## Issues ##

### How to draw rectangle in XML? ###

We can create a new XML file inside the drawable folder, and add the following code, then save it as rectangle.xml.

	<?xml version="1.0" encoding="utf-8"?>
	<shape xmlns:android="http://schemas.android.com/apk/res/android" >
	    <solid
	        android:color="@android:color/transparent" />
	    <stroke
	        android:width="2dip"
	        android:dashWidth="2dp"   
	        android:dashGap="5dp"    
	        android:color="#ff0000"/>
	</shape>

To use it inside a layout we would set the **android:background** attribute to the new drawable shape,like the following code segment.

	<ImageView
		android:id="@+id/rectimage"
		android:layout_height="100dp"
		android:layout_width="100dp"
		android:src="@drawable/rectangle">
	</ImageView>

finally,have a fun!

**Link:** [http://stackoverflow.com/questions/10124919/can-i-draw-rectangle-in-xml](http://stackoverflow.com/questions/10124919/can-i-draw-rectangle-in-xml "Can I draw rectangle in XML?")

### How to change "shape"(in XML) color dynamically? ###

The "Shape" code in circle2.xml is as like the following segments:

    <?xml version="1.0" encoding="utf-8"?>
	<shape
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    android:id="@+id/shape_circle2"
	    android:shape="oval"
	    android:useLevel="false" >	        
	    <solid
	        android:color="@android:color/transparent" />	    
	    <stroke
	        android:width="1dp"
	        android:color="#00ff00"/>
	    <size
	        android:width="55dp"
	        android:height="55dp"/>    
	</shape>

The code using "Shape" is as follows:

	<ImageView
		android:id="@+id/circle_img"
		android:layout_height="50dp"
		android:layout_width="50dp"
		android:background="@drawable/circle">
	</ImageView>

And we can modify the "Shape" color simply like this:

	ImageView imgviewCircle  = (ImageView)findViewById(R.id.circle_img);
	GradientDrawable backgroundGradient = (GradientDrawable)imgviewCircle.getBackground();
	backgroundGradient.setColor(Color.GREEN);

**Note:** It must be the attribute android:background of ImageView that use the "Shape" as long as we modify the "Shape" color like that!

**Link:** [http://stackoverflow.com/questions/7164630/how-to-change-shape-color-dynamically](http://stackoverflow.com/questions/7164630/how-to-change-shape-color-dynamically "How to change shape color dynamically?")

### Android设置textView水平居中显示

* 让textView里面的内容水平居中：android:gravity="center_horizontal"
* 让textView控件在它的父布局里水平居中：android:layout_gravity="center_horizontal"

### Android Camera Preview Stretched

**Link:** [http://stackoverflow.com/questions/19577299/android-camera-preview-stretched](http://stackoverflow.com/questions/19577299/android-camera-preview-stretched)

### Using lists in Android (ListView) - Tutorial

**Link:** [http://www.vogella.com/tutorials/AndroidListView/article.html](http://www.vogella.com/tutorials/AndroidListView/article.html)

### 两个Activity之间通过Intent传值

**Link:** [http://blog.csdn.net/aboy123/article/details/8567057](http://blog.csdn.net/aboy123/article/details/8567057)

### 解决Butter Knife空指针问题(在eclipse中使用)
[http://blog.163.com/www_iloveyou_com/blog/static/211658372201552421557741/](http://blog.163.com/www_iloveyou_com/blog/static/211658372201552421557741/)

### android实现顶部底部固定 中间可滑动
[http://blog.csdn.net/az44yao/article/details/7715509](http://blog.csdn.net/az44yao/article/details/7715509)
