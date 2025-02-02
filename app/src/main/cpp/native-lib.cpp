#include <jni.h>
#include <string>
#include <android/log.h>
#include "base64.h"
#include "aes.h"
#include <vector>

#define LOG_TAG "KeyUtils"





using namespace std;

vector<uint8_t> decodeBase64ToBytes(const string& base64String) {
    string decoded = base64_decode(base64String, true);
    return {decoded.begin(), decoded.end()};
}


// A helper function to remove PKCS#7 padding from the decrypted data.
static void remove_pkcs7_padding(vector<uint8_t>& data) {
    if (data.empty())
        return;
    // The last byte should contain the number of padding bytes.
    uint8_t pad = data.back();
    if (pad > 0 && pad <= 16 && pad <= data.size()) {
        // Optionally verify that all the padding bytes are equal to pad.
        bool valid = true;
        for (size_t i = data.size() - pad; i < data.size(); ++i) {
            if (data[i] != pad) {
                valid = false;
                break;
            }
        }
        if (valid) {
            data.resize(data.size() - pad);
        }
    }
}

static bool decodeBase64AndPrepareCipher(const string &encryptedBase64, vector<uint8_t> &cipherBytes) {
    // Decode the Base64 string (returns a string).
    string decoded = base64_decode(encryptedBase64, true);
    if (decoded.empty()) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Base64 decode failed!");
        return false;
    }

    // Convert the decoded string into a vector of bytes.
    cipherBytes.assign(decoded.begin(), decoded.end());

    // Ensure the cipher length is a multiple of the AES block size (16 bytes).
    if (cipherBytes.size() % 16 != 0) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Ciphertext size is not a multiple of 16!");
        return false;
    }
    return true;
}

static string decryptBase64EncodedString(const string &encryptedBase64) {
    // Log the received encrypted string.
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Base64 Encrypted String: %s", encryptedBase64.c_str());

    // Decode and prepare the cipher bytes.
    vector<uint8_t> cipherBytes;
    if (!decodeBase64AndPrepareCipher(encryptedBase64, cipherBytes)) {
        return "";
    }

    // Copy cipherBytes into a buffer for in-place decryption.
    vector<uint8_t> decryptedBytes = cipherBytes;

    // Initialize AES context with the key and IV.
    string aes_key_encrypted = ENCODED_AES_KEY;
    string aes_iv_encrypted = ENCODED_AES_IV;
    vector<uint8_t> AES_KEY = decodeBase64ToBytes(aes_key_encrypted);
    vector<uint8_t> AES_IV = decodeBase64ToBytes(aes_iv_encrypted);

    if (AES_KEY.size() != 32) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Decoded AES key size is invalid! Expected 32 bytes.");
    }
    if (AES_IV.size() != 16) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Decoded AES IV size is invalid! Expected 16 bytes.");
    }

    if (AES_KEY.empty() || AES_IV.empty()) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "AES key or IV is not initialized!");
        return "";
    }

    AES_ctx ctx;
    AES_init_ctx_iv(&ctx, AES_KEY.data(), AES_IV.data());

    // Decrypt the buffer in CBC mode.
    AES_CBC_decrypt_buffer(&ctx, decryptedBytes.data(), decryptedBytes.size());

    // Remove PKCS#7 padding (if present).
    remove_pkcs7_padding(decryptedBytes);

    // Convert the decrypted bytes to a string.
    string decryptedStr(decryptedBytes.begin(), decryptedBytes.end());
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Decrypted String: %s", decryptedStr.c_str());

    return decryptedStr;
}


// JNI functions
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_encryptedPassKey(JNIEnv *env, jobject thiz) {
    // Retrieve the Base64 encoded cipher text from the build-time define.
    string encryptedBase64 = ENCRYPTED_PASS_KEY;
    string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the ABHA Client Secret.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaClientSecret(JNIEnv *env, jobject thiz) {
    string encryptedBase64 = ABHA_CLIENT_SECRET;
    string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the ABHA Client ID.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaClientID(JNIEnv *env, jobject thiz) {
    string encryptedBase64 = ABHA_CLIENT_ID;
    string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the Base TMC URL.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_baseTMCUrl(JNIEnv *env, jobject thiz) {
    string encryptedBase64 = BASE_TMC_URL;
    string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the Base ABHA URL.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_baseAbhaUrl(JNIEnv *env, jobject thiz) {
    string encryptedBase64 = BASE_ABHA_URL;
    string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the ABHA Token URL.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaTokenUrl(JNIEnv *env, jobject thiz) {
    string encryptedBase64 = ABHA_TOKEN_URL;
    string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    return env->NewStringUTF(decryptedStr.c_str());
}

//---------------------------------------------------------------------------
// JNI function to retrieve the ABHA Auth URL.
extern "C"
JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaAuthUrl(JNIEnv *env, jobject thiz) {
    string encryptedBase64 = ABHA_AUTH_URL;
    string decryptedStr = decryptBase64EncodedString(encryptedBase64);
    return env->NewStringUTF(decryptedStr.c_str());
}
