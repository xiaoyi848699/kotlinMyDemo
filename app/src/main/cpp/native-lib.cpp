#include <jni.h>
#include <string>
#include "MyMD5.h"
#include "MyRSA.h"
#include "MyBASE64.h"
#include "MyAES.h"
#include "My3DES.h"

extern "C" {
    __attribute ((visibility ("default")))
    JNIEXPORT jstring JNICALL
    Java_demo_xy_com_xytdcq_Jni1Activity_stringFromJNI(
            JNIEnv *env,
            jobject /* this */) {
        std::string hello = "Hello from C++";
        return env->NewStringUTF(hello.c_str());
    }

    /**
     * MD5加密算法
     */
    __attribute ((visibility ("default")))
    JNIEXPORT jstring JNICALL
    Java_demo_xy_com_xytdcq_Jni1Activity_stringToMD5(JNIEnv *env, jobject instance, jstring msg_) {
        const char *msg = env->GetStringUTFChars(msg_, 0);

        std::string msgC;
        msgC.assign(msg);

        std::string f = MyMD5::encryptMD5(msgC);

        env->ReleaseStringUTFChars(msg_, msg);

        return env->NewStringUTF(f.c_str());
    }

    /**
     * RSA解密算法
     */
    __attribute ((visibility ("default")))
    JNIEXPORT jstring JNICALL
    Java_demo_xy_com_xytdcq_Jni1Activity_decryptRSA(JNIEnv *env, jobject instance, jstring msg_) {
        const char *msg = env->GetStringUTFChars(msg_, 0);

        std::string msgC;
        msgC.assign(msg);

        std::string base64 = MyBASE64::base64_decodestring(msgC);

        std::string rsa = MyRSA::decryptRSA(base64);


        env->ReleaseStringUTFChars(msg_, msg);

        return env->NewStringUTF(rsa.c_str());
    }


    /**
     * RSA  加密算法
     */
    __attribute ((visibility ("default")))
    JNIEXPORT jstring JNICALL
    Java_demo_xy_com_xytdcq_Jni1Activity_encryptRSA(JNIEnv *env, jobject instance, jstring msg_) {


        const char *msg = env->GetStringUTFChars(msg_, 0);

        std::string msgC;
        msgC.assign(msg);

        std::string rsa = MyRSA::encryptRSA(msgC, NULL);


        std::string base64 = MyBASE64::base64_encodestring(rsa);

        env->ReleaseStringUTFChars(msg_, msg);


        return env->NewStringUTF(base64.c_str());

    }

    /**
     * base64加密
     */

    __attribute ((visibility ("default")))
    JNIEXPORT jstring JNICALL
    Java_demo_xy_com_xytdcq_Jni1Activity_encryptBase64(JNIEnv *env, jobject instance, jstring msg_) {
        const char *msg = env->GetStringUTFChars(msg_, 0);

        std::string msgC;
        msgC.assign(msg);


        std::string base64 = MyBASE64::base64_encodestring(msgC);

        env->ReleaseStringUTFChars(msg_, msg);

        return env->NewStringUTF(base64.c_str());
    }

    /**
     * base64 解密
     */
    __attribute ((visibility ("default")))
    JNIEXPORT jstring JNICALL
    Java_demo_xy_com_xytdcq_Jni1Activity_decryptBase64(JNIEnv *env, jobject instance, jstring msg_) {
        const char *msg = env->GetStringUTFChars(msg_, 0);

        std::string msgC;
        msgC.assign(msg);


        std::string base64 = MyBASE64::base64_decodestring(msgC);

        env->ReleaseStringUTFChars(msg_, msg);

        return env->NewStringUTF(base64.c_str());
    }

    /**
     * AES加密算法
     */
    __attribute ((visibility ("default")))
    JNIEXPORT jstring JNICALL
    Java_demo_xy_com_xytdcq_Jni1Activity_encodeAES(JNIEnv *env, jobject instance, jstring msg_) {
        const char *msg = env->GetStringUTFChars(msg_, 0);

        std::string msgC;
        msgC.assign(msg);


        std::string aes = MyAES::encodeAES("1234567812345678", msgC);//密码长度必须大于16 位


        std::string base64 = MyBASE64::base64_encodestring(aes);


        env->ReleaseStringUTFChars(msg_, msg);

        return env->NewStringUTF(base64.c_str());
    }




    /**
     * AES解密算法
     */
    __attribute ((visibility ("default")))
    JNIEXPORT jstring JNICALL
    Java_demo_xy_com_xytdcq_Jni1Activity_decodeAES(JNIEnv *env, jobject instance, jstring msg_) {
        const char *msg = env->GetStringUTFChars(msg_, 0);

        std::string msgC;
        msgC.assign(msg);


        int length;
        std::string base64 = MyBASE64::base64_decodestring(msgC);


        std::string aes = MyAES::decodeAES("1234567812345678", base64);

        env->ReleaseStringUTFChars(msg_, msg);

        return env->NewStringUTF(aes.c_str());
    }

    /**
     * DES加密算法
     */


    __attribute ((visibility ("default")))
    JNIEXPORT jstring JNICALL
    Java_demo_xy_com_xytdcq_Jni1Activity_encryptDES(JNIEnv *env, jobject instance, jstring msg_) {
        const char *msg = env->GetStringUTFChars(msg_, 0);

        std::string msgC;
        msgC.assign(msg);

        int length;
        std::string key = "12345678";
        std::string des = My3DES::encryptDES(msgC, key, &length);


        std::string base64 = MyBASE64::base64_encodestring(des);


        env->ReleaseStringUTFChars(msg_, msg);

        return env->NewStringUTF(base64.c_str());
    }

    /**
     *
     * DES解密算法
     */
    __attribute ((visibility ("default")))
    JNIEXPORT jstring JNICALL
    Java_demo_xy_com_xytdcq_Jni1Activity_decryptDES(JNIEnv *env, jobject instance, jstring msg_) {
        const char *msg = env->GetStringUTFChars(msg_, 0);

        std::string msgC;
        msgC.assign(msg);

        std::string key = "12345678";
        int length;
        std::string base64 = MyBASE64::base64_decodestring(msgC);

        std::string des = My3DES::decryptDES(base64, key, base64.length());

        env->ReleaseStringUTFChars(msg_, msg);

        return env->NewStringUTF(des.c_str());
    }

}