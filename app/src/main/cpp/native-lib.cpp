#include <jni.h>
#include <string>
#include <android/log.h>
#include <openssl/evp.h>
#include <openssl/bio.h>
#include <openssl/err.h>
#include <vector>
#include <sys/ptrace.h>

// Log tag for debugging
#define LOG_TAG "KeyUtils"

// Logging helper macros
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

using namespace std;

/**
 * Base64 decoding function.
 * Decodes a base64-encoded string into a vector of unsigned characters (byte data).
 *
 * @param input The base64-encoded string to decode.
 * @return A vector of unsigned characters representing the decoded data.
 */
vector<unsigned char> base64_decode(const string &input) {
    // Check if the input is empty
    if (input.empty()) {
        LOGE("Base64 decode failed: Input string is empty.");
        return {};
    }

    // Set up BIO (Basic Input/Output) and base64 filter
    BIO *bio, *b64;
    int decodeLen = (input.size() * 3) / 4;
    vector<unsigned char> buffer(decodeLen);

    bio = BIO_new_mem_buf(input.data(), input.size());
    if (!bio) {
        LOGE("Base64 decode failed: Failed to create BIO object.");
        return {};
    }
    
    b64 = BIO_new(BIO_f_base64());
    if (!b64) {
        LOGE("Base64 decode failed: Failed to create base64 filter BIO.");
        BIO_free(bio);
        return {};
    }

    bio = BIO_push(b64, bio);
    decodeLen = BIO_read(bio, buffer.data(), buffer.size());
    
    // Check for errors during decoding
    if (decodeLen < 0) {
        LOGE("Base64 decode failed: Error during decoding.");
    } else if (decodeLen != buffer.size()) {
        LOGE("Base64 decode failed: Decoded data size mismatch.");
    }

    BIO_free_all(bio);
    return buffer;
}

/**
 * AES decryption function.
 * Decrypts an AES-encrypted string using AES-256-GCM and a predefined base64 key.
 * The decryption process includes the extraction of the IV, the encrypted data, and the tag.
 * 
 * @param encrypted The base64-encoded encrypted string.
 * @return The decrypted plaintext string if successful; otherwise, a failure message.
 */
string decryptAES(const string &encrypted) {
    // Check if the input is empty
    if (encrypted.empty()) {
        LOGE("AES decryption failed: Encrypted input string is empty.");
        return "DECRYPTION_FAILED: Encrypted input is empty.";
    }

    string decrypted;
    LOGD("AES decryption started...");

    // Decode the base64 key and encrypted input
    vector<unsigned char> key = base64_decode(FIXED_AES_KEY);
    vector<unsigned char> encryptedData = base64_decode(encrypted);

    if (key.empty()) {
        LOGE("AES decryption failed: Key decoding failed.");
        return "DECRYPTION_FAILED: Key decoding failed.";
    }

    if (encryptedData.empty()) {
        LOGE("AES decryption failed: Encrypted data decoding failed.");
        return "DECRYPTION_FAILED: Encrypted data decoding failed.";
    }

    // Extract IV (first 12 bytes for AES-GCM)
    vector<unsigned char> iv(encryptedData.begin(), encryptedData.begin() + 12);
    vector<unsigned char> encryptedSecret(encryptedData.begin() + 12, encryptedData.end() - 16);
    vector<unsigned char> tag(encryptedData.end() - 16, encryptedData.end());

    // Log details about the IV, encrypted secret, and tag
    LOGD("AES decryption details: IV: %s, Encrypted Secret Size: %zu, Tag Size: %zu",
         string(iv.begin(), iv.end()).c_str(), encryptedSecret.size(), tag.size());

    // AES decryption using AES-256-GCM
    EVP_CIPHER_CTX *ctx = EVP_CIPHER_CTX_new();
    if (!ctx) {
        LOGE("AES decryption failed: Failed to create EVP_CIPHER_CTX.");
        return "DECRYPTION_FAILED: EVP_CIPHER_CTX creation failed.";
    }

    if (EVP_DecryptInit_ex(ctx, EVP_aes_256_gcm(), NULL, NULL, NULL) != 1) {
        LOGE("AES decryption failed: EVP_DecryptInit_ex failed.");
        EVP_CIPHER_CTX_free(ctx);
        return "DECRYPTION_FAILED: EVP_DecryptInit_ex failed.";
    }

    if (EVP_CIPHER_CTX_ctrl(ctx, EVP_CTRL_GCM_SET_IVLEN, iv.size(), NULL) != 1) {
        LOGE("AES decryption failed: Failed to set IV length in EVP_CIPHER_CTX.");
        EVP_CIPHER_CTX_free(ctx);
        return "DECRYPTION_FAILED: IV length set failed.";
    }

    if (EVP_DecryptInit_ex(ctx, NULL, NULL, key.data(), iv.data()) != 1) {
        LOGE("AES decryption failed: Failed to initialize decryption context with key and IV.");
        EVP_CIPHER_CTX_free(ctx);
        return "DECRYPTION_FAILED: Decryption initialization failed.";
    }

    int len;
    decrypted.resize(encryptedSecret.size());
    if (EVP_DecryptUpdate(ctx, (unsigned char *)decrypted.data(), &len, encryptedSecret.data(), encryptedSecret.size()) != 1) {
        LOGE("AES decryption failed: EVP_DecryptUpdate failed.");
        EVP_CIPHER_CTX_free(ctx);
        return "DECRYPTION_FAILED: EVP_DecryptUpdate failed.";
    }

    if (EVP_CIPHER_CTX_ctrl(ctx, EVP_CTRL_GCM_SET_TAG, tag.size(), tag.data()) != 1) {
        LOGE("AES decryption failed: Failed to set GCM tag.");
        EVP_CIPHER_CTX_free(ctx);
        return "DECRYPTION_FAILED: Setting GCM tag failed.";
    }

    int ret = EVP_DecryptFinal_ex(ctx, (unsigned char *)decrypted.data() + len, &len);
    EVP_CIPHER_CTX_free(ctx);

    if (ret > 0) {
        LOGD("AES decryption succeeded.");
        return decrypted;  
    } else {
        LOGE("AES decryption failed: EVP_DecryptFinal_ex failed.");
        return "DECRYPTION_FAILED: EVP_DecryptFinal_ex failed.";
    }
}

/**
 * Check if a debugger is attached to the current process.
 *
 * @return true if a debugger is attached, false otherwise.
 */
bool isDebuggerAttached() {
    return ptrace(PTRACE_TRACEME, 0, 0, 0) == -1;
}

/** JNI Functions **/

// JNI method to check if the app is running on an emulator.
extern "C" JNIEXPORT jboolean JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_isEmulator(JNIEnv *env, jobject thiz) {
    const char* fingerprint = getenv("ro.build.fingerprint");
    bool isEmulator = (fingerprint != nullptr && string(fingerprint).find("generic") != string::npos);
    if (isEmulator) {
        LOGE("App is running on an emulator");
    }
    return isEmulator;
}

// JNI method to check if a debugger is attached.
extern "C" JNIEXPORT jboolean JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_isDebuggerAttached(JNIEnv *env, jobject thiz) {
    bool debuggerAttached = isDebuggerAttached();
    if (debuggerAttached) {
        LOGE("Debugger is attached to the process");
    }
    return debuggerAttached;
}

// JNI method to retrieve and decrypt the encrypted Pass Key.
extern "C" JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_encryptedPassKey(JNIEnv *env, jobject thiz) {
    string encryptedPassKey = ENCRYPTED_PASS_KEY;  
    if (encryptedPassKey.empty()) {
        LOGE("Encrypted Pass Key is empty. Cannot proceed with decryption.");
        return env->NewStringUTF("DECRYPTION_FAILED: Encrypted Pass Key is empty.");
    }
    LOGD("Decrypting Encrypted Pass Key...");
    string decryptedPassKey = decryptAES(encryptedPassKey);
    return env->NewStringUTF(decryptedPassKey.c_str());
}

// JNI method to decrypt and return the ABHA Client Secret
extern "C" JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaClientSecret(JNIEnv *env, jobject thiz) {
    
    string abhaClientSecret = ABHA_CLIENT_SECRET;  

    
    if (abhaClientSecret.empty()) {
        LOGE("ABHA Client Secret is empty. Cannot proceed with decryption.");
        return env->NewStringUTF("DECRYPTION_FAILED: ABHA Client Secret is empty.");
    }

    
    LOGD("Decrypting ABHA Client Secret...");

    
    string decryptedAbhaClientSecret = decryptAES(abhaClientSecret);

    
    return env->NewStringUTF(decryptedAbhaClientSecret.c_str());
}

// JNI method to decrypt and return the ABHA Client ID
extern "C" JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaClientID(JNIEnv *env, jobject thiz) {
    
    string abhaClientID = ABHA_CLIENT_ID;  

    
    if (abhaClientID.empty()) {
        LOGE("ABHA Client ID is empty. Cannot proceed with decryption.");
        return env->NewStringUTF("DECRYPTION_FAILED: ABHA Client ID is empty.");
    }

    
    LOGD("Decrypting ABHA Client ID...");

    
    string decryptedAbhaClientID = decryptAES(abhaClientID);

    
    return env->NewStringUTF(decryptedAbhaClientID.c_str());
}

// JNI method to return the base TMC URL
extern "C" JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_baseTMCUrl(JNIEnv *env, jobject thiz) {
    
    string baseTMCUrl = BASE_TMC_URL;

    
    if (baseTMCUrl.empty()) {
        LOGE("BASE_TMC_URL is empty. Cannot proceed with decryption.");
        return env->NewStringUTF("DECRYPTION_FAILED: BASE_TMC_URL is empty.");
    }

    
    LOGD("Decrypting BASE_TMC_URL...");

    
    return env->NewStringUTF(baseTMCUrl.c_str());
}

// JNI method to return the base ABHA URL
extern "C" JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_baseAbhaUrl(JNIEnv *env, jobject thiz) {
    
    string baseAbhaUrl = BASE_ABHA_URL;
    
    
    if (baseAbhaUrl.empty()) {
        LOGE("Base ABHA URL is empty. Cannot return value.");
        return env->NewStringUTF("ERROR: Base ABHA URL is empty.");
    }
    
    
    LOGD("Returning Base ABHA URL: %s", baseAbhaUrl.c_str());

    
    return env->NewStringUTF(baseAbhaUrl.c_str());
}

// JNI method to return the ABHA Token URL
extern "C" JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaTokenUrl(JNIEnv *env, jobject thiz) {
    
    string abhaTokenUrl = ABHA_TOKEN_URL;
    
    
    if (abhaTokenUrl.empty()) {
        LOGE("ABHA Token URL is empty. Cannot return value.");
        return env->NewStringUTF("ERROR: ABHA Token URL is empty.");
    }
    
    
    LOGD("Returning ABHA Token URL: %s", abhaTokenUrl.c_str());

    
    return env->NewStringUTF(abhaTokenUrl.c_str());
}

// JNI method to return the ABHA Authentication URL
extern "C" JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_abhaAuthUrl(JNIEnv *env, jobject thiz) {
    
    string abhaAuthUrl = ABHA_AUTH_URL;
    
    
    if (abhaAuthUrl.empty()) {
        LOGE("ABHA Auth URL is empty. Cannot return value.");
        return env->NewStringUTF("ERROR: ABHA Auth URL is empty.");
    }
  
    LOGD("Returning ABHA Auth URL: %s", abhaAuthUrl.c_str());

  =
    return env->NewStringUTF(abhaAuthUrl.c_str());
}

// JNI method to return the Chat URL
extern "C" JNIEXPORT jstring JNICALL
Java_org_piramalswasthya_sakhi_utils_KeyUtils_chatUrl(JNIEnv *env, jobject thiz) {
    
    string chatUrl = CHAT_URL;
    
    
    if (chatUrl.empty()) {
        LOGE("Chat URL is empty. Cannot return value.");
        return env->NewStringUTF("ERROR: Chat URL is empty.");
    }
    
    
    LOGD("Returning Chat URL: %s", chatUrl.c_str());

    
    return env->NewStringUTF(chatUrl.c_str());
}

