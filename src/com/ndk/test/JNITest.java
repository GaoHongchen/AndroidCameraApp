package com.ndk.test;

public class JNITest {
	
	static {
        System.loadLibrary("JNITest");
    }

	static public native String AddString(String strA, String strB);
    static public native int AddInt(int a, int b);
}
