package org.piramalswasthya.sakhi.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import org.piramalswasthya.sakhi.helpers.Languages
import java.text.NumberFormat
import java.util.Locale

class HelperUtil {

    fun getLocalizedResources(context: Context, currentLanguage: Languages): Resources {
        val desiredLocale = Locale(currentLanguage.symbol)
        var conf = context.resources.configuration
        conf = Configuration(conf)
        conf.setLocale(desiredLocale)
        val localizedContext: Context = context.createConfigurationContext(conf)
        return localizedContext.resources
    }

    fun getLocalizedContext(context: Context, currentLanguage: Languages): Context {
        val desiredLocale = Locale(currentLanguage.symbol)
        var conf = context.resources.configuration
        conf = Configuration(conf)
        conf.setLocale(desiredLocale)
        return context.createConfigurationContext(conf)
    }

    fun formatNumber(number: Int, languages: Languages): Int {
        val locale = Locale(languages.symbol)
        val numberFormatter = NumberFormat.getInstance(locale)
        return numberFormatter.format(number).replace(",","").toInt()
    }

}