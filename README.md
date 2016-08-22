# AndroidCameraApp
An Android Camera Project with ADT

------

## Development Requirements ##

- JDK
- Eclipse ==> eclipse-java-mars-R-win32
- ADT ==> ADT-23.0.7.zip
- Android SDK ==> installer_r24.4.1-windows.exe
- EGit ==> http://download.eclipse.org/egit/updates/

## Linking ##

* [http://www.cnblogs.com/zoupeiyang/p/4034517.html](http://www.cnblogs.com/zoupeiyang/p/4034517.html "五步搞定Android开发环境部署")
* [http://my.oschina.net/bbdlg/blog/115345?p={{page}}](http://my.oschina.net/bbdlg/blog/115345?p={{page}} "在android的eclipse开发环境中配置git环境")
* [http://www.cnblogs.com/bjzhanghao/archive/2012/11/14/android-platform-sdk-download-mirror.html](http://www.cnblogs.com/bjzhanghao/archive/2012/11/14/android-platform-sdk-download-mirror.html "Android SDK开发包国内下载地址")
* [https://github.com/inferjay/AndroidDevTools](https://github.com/inferjay/AndroidDevTools "AndroidDevTools")
* [http://www.ibm.com/developerworks/opensource/tutorials/os-eclipse-androidwidget/](http://www.ibm.com/developerworks/opensource/tutorials/os-eclipse-androidwidget/ "Introduction to Android development Using Eclipse and Android widgets")

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