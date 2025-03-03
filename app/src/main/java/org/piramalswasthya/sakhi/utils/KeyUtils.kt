package org.piramalswasthya.sakhi.utils

import org.piramalswasthya.sakhi.BuildConfig
import timber.log.Timber

/**
 * Secure configuration provider using JNI for production and BuildConfig for development.
 * Provides a single source of truth for sensitive configuration values with fallbacks
 * for different build flavors in development mode.
 *
 * @throws RuntimeException if native library fails to load
 */
object KeyUtils {
    private const val NATIVE_LIB_NAME = "sakhi"

    init {
        try {
            System.loadLibrary(NATIVE_LIB_NAME)
        } catch (e: UnsatisfiedLinkError) {
            Timber.tag("KeyUtils").e(e, "Native library load failed")
            throw RuntimeException("Failed to load native library: $NATIVE_LIB_NAME")
        }
    }

    // Native method declarations
    private external fun encryptedPassKey(): String
    private external fun abhaClientSecret(): String
    private external fun abhaClientID(): String
    private external fun baseTMCUrl(): String
    private external fun baseAbhaUrl(): String
    private external fun abhaTokenUrl(): String
    private external fun abhaAuthUrl(): String
    private external fun chatUrl(): String

    // Public API
    fun getEncryptedPassKey() = getConfigValue(ConfigType.ENCRYPTED_PASS_KEY)
    fun getAbhaClientSecret() = getConfigValue(ConfigType.ABHA_CLIENT_SECRET)
    fun getAbhaClientID() = getConfigValue(ConfigType.ABHA_CLIENT_ID)
    fun getBaseTMCUrl() = getConfigValue(ConfigType.BASE_TMC_URL)
    fun getBaseAbhaUrl() = getConfigValue(ConfigType.BASE_ABHA_URL)
    fun getAbhaTokenUrl() = getConfigValue(ConfigType.ABHA_TOKEN_URL)
    fun getAbhaAuthUrl() = getConfigValue(ConfigType.ABHA_AUTH_URL)
    fun getChatUrl() = getConfigValue(ConfigType.CHAT_URL)

    private fun getConfigValue(type: ConfigType): String {
        return if (isDevelopmentBuild()) {
            getDevelopmentConfig(type)
        } else {
            getProductionConfig(type)
        }
    }

    private fun getProductionConfig(type: ConfigType): String = when (type) {
        ConfigType.ENCRYPTED_PASS_KEY -> encryptedPassKey()
        ConfigType.ABHA_CLIENT_SECRET -> abhaClientSecret()
        ConfigType.ABHA_CLIENT_ID -> abhaClientID()
        ConfigType.BASE_TMC_URL -> baseTMCUrl()
        ConfigType.BASE_ABHA_URL -> baseAbhaUrl()
        ConfigType.ABHA_TOKEN_URL -> abhaTokenUrl()
        ConfigType.ABHA_AUTH_URL -> abhaAuthUrl()
        ConfigType.CHAT_URL -> chatUrl()
    }

    private fun getDevelopmentConfig(type: ConfigType): String {
        val flavorPrefix = when (BuildConfig.FLAVOR) {
            "sakshamStag" -> "SAKSHAMSTAG"
            "sakshamUat" -> "SAKSHAMUAT"
            "saksham" -> "SAKSHAM"
            "xushrukha" -> "XUSHRUKHA"
            "niramay" -> "NIRAMAY"
            else -> return ""
        }

        return when (type) {
            ConfigType.ENCRYPTED_PASS_KEY -> getBuildConfigField("${flavorPrefix}_ENCRYPTED_PASS_KEY")
            ConfigType.ABHA_CLIENT_SECRET -> getBuildConfigField("${flavorPrefix}_ABHA_CLIENT_SECRET")
            ConfigType.ABHA_CLIENT_ID -> getBuildConfigField("${flavorPrefix}_ABHA_CLIENT_ID")
            ConfigType.BASE_TMC_URL -> getBuildConfigField("${flavorPrefix}_BASE_TMC_URL")
            ConfigType.BASE_ABHA_URL -> getBuildConfigField("${flavorPrefix}_BASE_ABHA_URL")
            ConfigType.ABHA_TOKEN_URL -> getBuildConfigField("${flavorPrefix}_ABHA_TOKEN_URL")
            ConfigType.ABHA_AUTH_URL -> getBuildConfigField("${flavorPrefix}_ABHA_AUTH_URL")
            ConfigType.CHAT_URL -> getBuildConfigField("${flavorPrefix}_CHAT_URL")
        }
    }

    private fun getBuildConfigField(fieldName: String): String {
        return try {
            BuildConfig::class.java.getField(fieldName).get(null) as String
        } catch (e: Exception) {
            Timber.e("Missing BuildConfig field: $fieldName")
            ""
        }
    }

    private fun isDevelopmentBuild() = BuildConfig.IS_DEVELOPEMENT.toBoolean()

    private enum class ConfigType {
        ENCRYPTED_PASS_KEY,
        ABHA_CLIENT_SECRET,
        ABHA_CLIENT_ID,
        BASE_TMC_URL,
        BASE_ABHA_URL,
        ABHA_TOKEN_URL,
        ABHA_AUTH_URL,
        CHAT_URL
    }
}