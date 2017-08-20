APP_STL := c++_static #gnustl_static gnustl_shared c++_static

APP_ABI := armeabi-v7a

APP_CPPFLAGS := -frtti -fexceptions -ftree-vectorize -Wno-deprecated -pthread -stdlib=libc++ -std=c++11 -o3\
				 #-mfloat-abi=softfp -o3 -0fast --ffast-math  -stdlib=libc++ -std=c++0x
