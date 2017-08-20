#include "com_ndk_test_JNITest.h"
#include <stdlib.h>
#include <stdio.h>

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_ndk_test_JNITest
 * Method:    AddString
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_ndk_test_JNITest_AddString(JNIEnv *pEnv, jclass arg, jstring strA, jstring strB)
{
    jstring str = (*pEnv)->NewStringUTF(pEnv, "HelloWorld from JNI !");
    return str;
}

/*
 * Class:     com_ndk_test_JNITest
 * Method:    AddInt
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_ndk_test_JNITest_AddInt(JNIEnv *pEnv, jclass arg, jint a, jint b)
{
	return a+b;
}

#ifdef __cplusplus
}
#endif
