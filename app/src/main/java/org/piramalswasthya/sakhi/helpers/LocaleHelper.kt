package org.piramalswasthya.sakhi.helpers

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocaleHelper @Inject constructor(
    private val prefDao: PreferenceDao
) {
    enum class Languages(val symbol: String) {
        ENGLISH("en"),
        HINDI("hi"),
        ASSAMESE("as")
    }

    fun setLocale(context: Context, language: Languages): Context {
        Timber.d("Setting Locale : $language")
        prefDao.saveSetLanguage(language)

        // updating the language for devices above android nougat
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language.symbol)
        } else updateResourcesLegacy(context, language.symbol)
        // for devices having lower version of android os
    }

    fun getLocale(): Languages {
        return prefDao.getCurrentLanguage()
    }


    // the method is used update the language of application by creating
    // object of inbuilt Locale class and passing language argument to it
    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        //Locale.setDefault(locale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        //Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}