#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "KeyUtils"


// JNI functions
extern "C" JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_encryptedPassKey(JNIEnv *env, jobject thiz) {
    std::string encryptedPassKey = ENCRYPTED_PASS_KEY;
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Encrypted Password Key: %s",
                        encryptedPassKey.c_str());
    return env->NewStringUTF(encryptedPassKey.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaClientSecret(JNIEnv *env, jobject thiz) {
    std::string abhaClientSecret = ABHA_CLIENT_SECRET;
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "ABHA Client Secret: %s",
                        abhaClientSecret.c_str());
    return env->NewStringUTF(abhaClientSecret.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaClientID(JNIEnv *env, jobject thiz) {
    std::string abhaClientID = ABHA_CLIENT_ID;
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "ABHA Client ID: %s", abhaClientID.c_str());
    return env->NewStringUTF(abhaClientID.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_baseTMCUrl(JNIEnv *env, jobject thiz) {
    std::string baseTMCUrl = BASE_TMC_URL;
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Base TMC URL: %s", baseTMCUrl.c_str());
    return env->NewStringUTF(baseTMCUrl.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_baseAbhaUrl(JNIEnv *env, jobject thiz) {
    std::string baseAbhaUrl = BASE_ABHA_URL;
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Base ABHA URL: %s", baseAbhaUrl.c_str());
    return env->NewStringUTF(baseAbhaUrl.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaTokenUrl(JNIEnv *env, jobject thiz) {
    std::string abhaTokenUrl = ABHA_TOKEN_URL;
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "ABHA Token URL: %s", abhaTokenUrl.c_str());
    return env->NewStringUTF(abhaTokenUrl.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaAuthUrl(JNIEnv *env, jobject thiz) {
    std::string abhaAuthUrl = ABHA_AUTH_URL;
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "ABHA Auth URL: %s", abhaAuthUrl.c_str());
    return env->NewStringUTF(abhaAuthUrl.c_str());
}
