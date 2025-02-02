#include <jni.h>
#include <string>
#include <android/log.h>

using namespace std;


// JNI functions
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_encryptedPassKey(JNIEnv *env, jobject thiz) {
    // Retrieve the Base64 encoded cipher text from the build-time define.
    // string encryptedBase64 = ENCRYPTED_PASS_KEY;
    // string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    string decryptedStr = ENCRYPTED_PASS_KEY;
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the ABHA Client Secret.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaClientSecret(JNIEnv *env, jobject thiz) {
    // string encryptedBase64 = ABHA_CLIENT_SECRET;
    // string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    string decryptedStr = ABHA_CLIENT_SECRET;
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the ABHA Client ID.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaClientID(JNIEnv *env, jobject thiz) {
    // string encryptedBase64 = ABHA_CLIENT_ID;
    // string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    string decryptedStr = ABHA_CLIENT_ID;
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the Base TMC URL.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_baseTMCUrl(JNIEnv *env, jobject thiz) {
    // string encryptedBase64 = BASE_TMC_URL;
    // string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    string decryptedStr = BASE_TMC_URL;
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the Base ABHA URL.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_baseAbhaUrl(JNIEnv *env, jobject thiz) {
    // string encryptedBase64 = BASE_ABHA_URL;
    // string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    string decryptedStr = BASE_ABHA_URL;
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the ABHA Token URL.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaTokenUrl(JNIEnv *env, jobject thiz) {
    // string encryptedBase64 = ABHA_TOKEN_URL;
    // string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    string decryptedStr = ABHA_TOKEN_URL;
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the ABHA Auth URL.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaAuthUrl(JNIEnv *env, jobject thiz) {
    // string encryptedBase64 = ABHA_AUTH_URL;
    // string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    string decryptedStr = ABHA_AUTH_URL;
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the ABHA Auth URL.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_chatUrl(JNIEnv *env, jobject thiz) {
    // string encryptedBase64 = ABHA_AUTH_URL;
    // string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    string decryptedStr = CHAT_URL;
    return env->NewStringUTF(decryptedStr.c_str());
}