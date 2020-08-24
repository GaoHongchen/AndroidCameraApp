package com.ndk.test;

public class OpenCVTest {
    
    static {
        System.loadLibrary("OpenCVTest");
    }
    
    static public native void test();
}