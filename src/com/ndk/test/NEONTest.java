package com.ndk.test;

public class NEONTest {

	static {
        System.loadLibrary("NEONTest");
    }
	
	static public native void test();
}
