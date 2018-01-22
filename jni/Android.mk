LOCAL_PATH := $(call my-dir)
JNI_PATH := $(LOCAL_PATH)

include $(call all-subdir-makefiles)

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
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
LOCAL_STATIC_LIBRARIES := libfftw3f
LOCAL_SRC_FILES := fftw3/com_ndk_test_FFTW3.cc Tools/types.cc
LOCAL_CPPFLAGS += -std=c++11
LOCAL_CPPFLAGS += -D__cplusplus=201103L
include $(BUILD_SHARED_LIBRARY)
