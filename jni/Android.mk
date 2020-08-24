LOCAL_PATH := $(call my-dir)
JNI_PATH := $(LOCAL_PATH)

# include $(call all-subdir-makefiles)

##### JNITest
LOCAL_PATH = $(JNI_PATH)
include $(CLEAR_VARS)
LOCAL_MODULE := JNITest
LOCAL_SRC_FILES := JNITest/com_ndk_test_JNITest.c 
include $(BUILD_SHARED_LIBRARY)

##### FFTW3
LOCAL_PATH = $(JNI_PATH)
include $(CLEAR_VARS)
LOCAL_MODULE := FFTW3
LOCAL_CPPFLAGS := -w # ignore warning
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
LOCAL_STATIC_LIBRARIES := libfftw3f
LOCAL_SRC_FILES := FFTW3/com_ndk_test_FFTW3.cc Tools/types.cc
include $(BUILD_SHARED_LIBRARY)

##### NEONTest
LOCAL_PATH = $(JNI_PATH)
include $(CLEAR_VARS)
LOCAL_MODULE := NEONTest
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
LOCAL_SRC_FILES := NEONTest/com_ndk_test_NEONTest.cc
LOCAL_CPPFLAGS += -fno-tree-vectorize
include $(BUILD_SHARED_LIBRARY)

##### OpenCVTest
LOCAL_PATH = $(JNI_PATH)
include $(CLEAR_VARS)
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED
# OPENCV_LIB_TYPE:=STATIC
ifeq ("$(wildcard $(OPENCV_MK_PATH))","")  
    include /opt/opencv_android_sdk/sdk/native/jni/OpenCV.mk
else  
    include $(OPENCV_MK_PATH)
endif
LOCAL_MODULE := OpenCVTest
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog
LOCAL_SRC_FILES += OpenCVTest/com_ndk_test_OpenCVTest.cpp
LOCAL_CPPFLAGS += -fno-tree-vectorize
include $(BUILD_SHARED_LIBRARY)

$(call import-add-path, $(JNI_PATH)/ndk-modules)
$(call import-module, fftw3)
