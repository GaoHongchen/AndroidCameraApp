#ifndef _Included_types
#define _Included_types

#include <jni.h>

#include <stdlib.h>
#include <stdio.h>
#include <string>

jstring pchar2jstring(JNIEnv* env, const char* pat);
char* jstring2pchar(JNIEnv* env, jstring jstr);

std::string jstring2str(JNIEnv* env, jstring jstr);

#endif
