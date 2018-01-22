APP_STL := c++_static #gnustl_static gnustl_shared c++_static

APP_ABI := armeabi-v7a

APP_CPPFLAGS := -frtti -fexceptions -ftree-vectorize -Wno-deprecated -pthread -std=c++11 -O3 \
                -mfloat-abi=softfp -mfpu=neon -Ofast -ffast-math -fno-finite-math-only
