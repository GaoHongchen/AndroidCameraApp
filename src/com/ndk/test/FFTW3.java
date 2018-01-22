package com.ndk.test;

public class FFTW3 {
	
	static {
        System.loadLibrary("FFTW3");
    }

	static public native void DFT2DfromPath(String imgPath);
	
	static public native void DFT2DfromAddr(long matAddrIn);
}
