#include <jni.h>
#include <string>
#include "MyMD5.h"
#include "MyRSA.h"
#include "MyBASE64.h"

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
}