#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define MYLOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"xy",FORMAT,__VA_ARGS__)
#define MYLOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"xy",FORMAT,__VA_ARGS__)

char password[] = "password";
//加密文件
JNIEXPORT void JNICALL
Java_demo_xy_com_xytdcq_nkd_interfaceI_NDKInterface_00024Companion_cryptorEncrypt(JNIEnv *env,  jobject job, jstring normal_path_jstr,jstring encrypt_path_jstr) {
    MYLOGE("cryptorEncrypt:%s","1");
    //jstring -> char*
    const char* normal_path = (*env)->GetStringUTFChars(env,normal_path_jstr,NULL);
    const char* encrypt_path = (*env)->GetStringUTFChars(env,encrypt_path_jstr,NULL);
    MYLOGE("cryptorEncrypt:%s","2");
    //打开文件
    FILE *normal_fp = fopen(normal_path, "rb");
    FILE *crypt_fp = fopen(encrypt_path, "wb");
    //一次读取一个字符
    int ch;
    int i = 0; //循环使用密码中的字母进行异或运算
    int pwd_len = strlen(password); //密码的长度
    while ((ch = fgetc(normal_fp)) != EOF) { //End of File
        //写入（异或运算）
        fputc(ch ^ password[i % pwd_len], crypt_fp);
        i++;
    }
    //关闭
    fclose(crypt_fp);
    fclose(normal_fp);
    //释放
    (*env)->ReleaseStringUTFChars(env,encrypt_path_jstr,encrypt_path);
    (*env)->ReleaseStringUTFChars(env,normal_path_jstr,normal_path);
}

//解密文件
JNIEXPORT void JNICALL
Java_demo_xy_com_xytdcq_nkd_interfaceI_NDKInterface_00024Companion_cryptorDecrypt(JNIEnv *env, jobject job, jstring encrypt_path_jstr, jstring decrypt_path_jstr) {
    const char* encrypt_path = (*env)->GetStringUTFChars(env,encrypt_path_jstr,NULL);
    const char* decrypt_path = (*env)->GetStringUTFChars(env,decrypt_path_jstr,NULL);

    //打开文件
    FILE *normal_fp = fopen(encrypt_path, "rb");
    FILE *crypt_fp = fopen(decrypt_path, "wb");
    //一次读取一个字符
    int ch;
    int i = 0; //循环使用密码中的字母进行异或运算
    int pwd_len = strlen(password); //密码的长度
    while ((ch = fgetc(normal_fp)) != EOF) { //End of File
        //写入（异或运算）
        fputc(ch ^ password[i % pwd_len], crypt_fp);
        i++;
    }
    //关闭
    fclose(crypt_fp);
    fclose(normal_fp);
    //释放
    (*env)->ReleaseStringUTFChars(env,decrypt_path_jstr,decrypt_path);
    (*env)->ReleaseStringUTFChars(env,encrypt_path_jstr,encrypt_path);
}

