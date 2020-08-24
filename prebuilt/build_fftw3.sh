#!/bin/sh 
set -o errexit

# Compiles fftw3 for Android
# Make sure you have ANDROID_NDK defined in .bashrc or .bash_profile

PROJECT_ROOT="`pwd`/../"
INSTALL_DIR="$PROJECT_ROOT/jni/ndk-modules/fftw3"
mkdir -p $INSTALL_DIR

NDK_DIR=${ANDROID_NDK}

export PATH="$NDK_DIR/toolchains/arm-linux-androideabi-4.9/prebuilt/linux-x86_64/bin/:$PATH"
export SYS_ROOT="$NDK_DIR/platforms/android-9/arch-arm/"
export CC="arm-linux-androideabi-gcc --sysroot=$SYS_ROOT"
export CXX="arm-linux-androideabi-g++"
export LD="arm-linux-androideabi-ld"
export AR="arm-linux-androideabi-ar"
export RANLIB="arm-linux-androideabi-ranlib"
export STRIP="arm-linux-androideabi-strip"
export CFLAGS="-mfpu=neon -march=armv7-a -mfloat-abi=softfp" #use Thumb-2 instructs
export CXXFLAGS="-lstdc++"
export LDFLAGS="-lc -lgcc"

SRC_DIR="./fftw-3.3.3"
cd $SRC_DIR

##### configure & make & make install #####

./configure --host=arm-eabi --prefix=$INSTALL_DIR --enable-float --enable-neon

make -j$(nproc)
make install -j$(nproc)

exit 0
