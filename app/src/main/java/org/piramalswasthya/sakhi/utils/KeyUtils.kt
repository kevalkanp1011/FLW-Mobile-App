package org.piramalswasthya.sakhi.utils

import android.util.Log
import timber.log.Timber


/**
 * Security utility for retrieving sensitive configuration values through native code.
 * This class provides a secure interface to access sensitive information such as keys,
 * secrets, and URLs by leveraging native code to prevent reverse engineering.
 *
 * Note: This class requires the native library 'sakhi' to be properly configured
 * and built using CMake. Ensure the native implementation follows security best
 * practices for handling sensitive data.
 */
object KeyUtils {

    private const val NATIVE_JNI_LIB_NAME = "sakhi"

    init {
        try {
            System.loadLibrary(NATIVE_JNI_LIB_NAME)
            Timber.tag("KeyUtils").d(encryptedPassKey())
            Timber.tag("KeyUtils").d(abhaClientSecret())
            Timber.tag("KeyUtils").d(abhaClientID())
        } catch (e: UnsatisfiedLinkError) {
            Timber.tag("KeyUtils").e(e, "Failed to load native library")
            throw RuntimeException("Failed to load native library: $NATIVE_JNI_LIB_NAME")
        }

    }

//    private external fun isDebuggerAttached(): Boolean
//    private external fun isEmulator(): Boolean
//
//    // Example usage
//    fun checkSecurity() {
//        if (isDebuggerAttached()) {
//            Timber.tag("KeyUtils").e("Debugger detected! Taking action...")
//            // Handle debugger detection logic here (e.g., terminate app, prevent access to sensitive data)
//        } else {
//            Timber.tag("KeyUtils").d("No debugger detected.")
//        }
//
//        if (isEmulator()) {
//            Timber.tag("KeyUtils").e("Running on emulator! Taking action...")
//            // Handle emulator detection logic here (e.g., prevent running on emulators)
//        } else {
//            Timber.tag("KeyUtils").d("Running on a real device.")
//        }
//    }

    external fun encryptedPassKey(): String

    external fun abhaClientSecret(): String

    external fun abhaClientID(): String

    external fun baseTMCUrl(): String

    external fun baseAbhaUrl(): String

    external fun abhaTokenUrl(): String

    external fun abhaAuthUrl(): String

    external fun chatUrl(): String


}



