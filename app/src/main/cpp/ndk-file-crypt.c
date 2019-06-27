#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <jni.h>
#include <android/log.h>

#define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"xy",FORMAT,__VA_ARGS__)
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"xy",FORMAT,__VA_ARGS__)

char password[] = "password";
//加密demo.xy.com.xytdcq.nkd.encryption
JNIEXPORT void JNICALL
Java_demo_xy_com_xytdcq_nkd_encryption_EncryptionFile_00024Companion_CryptorEncrypt(JNIEnv *env,  jclass jcls, jstring normal_path_jstr,jstring encrypt_path_jstr) {
//    LOGE("CryptorEncrypt:%s","1");
    //jstring -> char*
    const char* normal_path = (*env)->GetStringUTFChars(env,normal_path_jstr,NULL);
    const char* encrypt_path = (*env)->GetStringUTFChars(env,encrypt_path_jstr,NULL);
//    LOGE("CryptorEncrypt:%s","2");
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

//解密
JNIEXPORT void JNICALL
Java_demo_xy_com_xytdcq_nkd_encryption_EncryptionFile_00024Companion_CryptorDecrypt(JNIEnv *env, jclass jcls, jstring encrypt_path_jstr, jstring decrypt_path_jstr) {
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

