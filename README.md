# Android Camera App

An Android Camera Project.

------

## Requirements

### CImg

**Note:**  
Add the following code in CImg.h from https://github.com/dtschump/CImg.git to NOT use Xlib.h:  
```
#undef cimg_display
#define cimg_display 0
```

### FFTW

It is best to download official tarballs from http://fftw.org/, other than using its github repository!!!    
Build Reference: [fftw_android( from he-kai github )](https://github.com/hekai/fftw_android)

### OpenCV for Android

[http://opencv.org/platforms/android.html](http://opencv.org/platforms/android.html "OpenCV for Android")

opencv for android 教程（环境搭建篇）：
[http://blog.csdn.net/pwh0996/article/details/8957764](http://blog.csdn.net/pwh0996/article/details/8957764)


## Build & Install

### Environments Config

* in `.bashrc`
  ```sh
  export JAVA_HOME=/home/cg/tools/android_tools/jdk1.8.0_251
  export JRE_HOME=$JAVA_HOME/jre
  export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib
  export PATH=$JAVA_HOME/bin:$PATH

  export ANDROID_SDK=/home/cg/tools/android_tools/android-sdk-linux
  export PATH=$ANDROID_SDK/tools:$PATH
  export PATH=$ANDROID_SDK/platform-tools:$PATH

  export ANDROID_NDK=/home/cg/tools/android_tools/android-ndk-r16b
  export PATH=$ANDROID_NDK:$ANDROID_NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/linux-x86_64/bin:$PATH
  ```

### Generate .so or .a files using NDK tools

1. Edit java file;  
2. Generate header file for the java file using JDK tools **javac** and **javah**;  
3. Edit **Android.mk** and **Application.mk** files;  
4. Generate .so or .a files using command **ndk-build**;  
5. Load the library and using its methods;


### Build Android Project

#### Ant Project

1. generate build.xml for Ant: `android update project -p .`
2. build ant project: `ant debug/release`

#### Eclipse Project

* [Introduction to Android development Using Eclipse and Android widgets](http://www.ibm.com/developerworks/opensource/tutorials/os-eclipse-androidwidget/)

### Sign APK

1. Generate keystore file  
    ```sh
    keytool -genkey -alias ChenguangCam -keyalg RSA -validity 100000 -keystore AndroidCameraApp.keystore
    ```

2. Sign Apk

* 使用第三方工具：**爱加密签名工具**；  
* 使用命令行：  
  ```sh
  jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore  
  <path_to_AndroidCameraApp.keystore> -storepass 123456 -keypass 123456 -signedjar <path_to_signed.apk> <path_to_unsign.apk> ChenguangCam
  ```
* Eclipse Project: 右键单击项目名称，选择"Android Tools"，再选择"Export Signed Application Package…"；
* Ant Project: add **key.store** and **key.alias** properties to **ant.properties** file;

### Install APK

* Install apk file to devices
  ```sh
  adb install <path_to_apk>
  ```

### Debug

### 应用认领

应用认领那些事：   
[http://droidyue.com/blog/2014/12/14/android-yingyong-renling/?utm_source=tuicool&utm_medium=referral](http://droidyue.com/blog/2014/12/14/android-yingyong-renling/?utm_source=tuicool&utm_medium=referral)

