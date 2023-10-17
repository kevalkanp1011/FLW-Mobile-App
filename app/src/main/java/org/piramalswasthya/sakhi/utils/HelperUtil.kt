package org.piramalswasthya.sakhi.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.AgeUnitDTO
import java.lang.StringBuilder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object HelperUtil {

    private val dateFormat = SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault())

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
        Locale.setDefault(desiredLocale)
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

    fun getDateStringFromLong(dateLong: Long?): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        dateLong?.let {
            val dateString = dateFormat.format(dateLong)
            return dateString
        } ?: run {
            return null
        }

    }

    /**
     * gets millis from given years months days
     */
    fun getDobFromAge(ageUnit: AgeUnitDTO) : Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, 0 - ageUnit.years)
        cal.add(Calendar.MONTH, 0 - ageUnit.months)
        cal.add(Calendar.DAY_OF_MONTH, 0 - ageUnit.days)
        return cal.timeInMillis
    }

    /**
     * gets millis for date in dd-MM-yyyy format
     */
    fun getLongFromDate(dateString: String): Long {
        val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val date = f.parse(dateString)
        return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
    }

    /**
     * gets age string like -- 2 years, 3 months or 3 months, 4 days
     */
    fun getAgeStrFromAgeUnit(ageUnitDTO: AgeUnitDTO): String {
        val str = StringBuilder("")

        if (ageUnitDTO.years >= 1) {
            str.append(ageUnitDTO.years)
            str.append(if (ageUnitDTO.years == 1) " Year, " else " Years, ")
        }

        if (ageUnitDTO.months >= 1) {
            str.append(ageUnitDTO.months)
            str.append(if (ageUnitDTO.months == 1) " Month" else " Months")
        }

        if (ageUnitDTO.days >= 1 && ageUnitDTO.years < 1) {
            if (ageUnitDTO.months >= 1) str.append(", ")
            str.append(ageUnitDTO.days)
            str.append(if (ageUnitDTO.days == 1) " Day " else " Days ")
        }
        return str.toString()
    }

    /**
     * calculates age in years, months, days from calendar and sets it to age dto
     */
    fun updateAgeDTO(ageUnitDTO: AgeUnitDTO, cal: Calendar) {
        val calNow = Calendar.getInstance()
        val calNowLocal = Calendar.getInstance()
        var years = calNow.get(Calendar.YEAR) - cal.get(Calendar.YEAR)
        var months = calNow.get(Calendar.MONTH) - cal.get(Calendar.MONTH)
        if (months < 0) {
            years -= 1
            months += 12
        }

        var days = calNow.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH)
        if (days < 0) {
            if (months == 0) {
                years -= 1
                months += 11
                days += 30
            } else {
                months -= 1
                days += 30
            }
        }
        calNowLocal.set(Calendar.YEAR, calNow.get(Calendar.YEAR) - years)
        calNowLocal.set(Calendar.MONTH, calNow.get(Calendar.MONTH) - months)
        ageUnitDTO.years = years
        ageUnitDTO.months = months
        ageUnitDTO.days = days
    }


    fun getTrackDate(long: Long?): String? {
        long?.let {
            return " on ${dateFormat.format(long)}"
        }
        return null
    }
}