package org.piramalswasthya.sakhi.database.shared_preferences

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.LocationRecord
import org.piramalswasthya.sakhi.model.getDateTimeStringFromLong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDao @Inject constructor(@ApplicationContext private val context: Context) {

    private val pref = PreferenceManager.getInstance(context)

    fun getD2DApiToken(): String? {
        val prefKey = context.getString(R.string.PREF_D2D_API_KEY)
        return pref.getString(prefKey, null)
    }

    fun registerD2DApiToken(token: String) {
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_D2D_API_KEY)
        editor.putString(prefKey, token)
        editor.apply()
    }

    fun deletePrimaryApiToken() {
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_D2D_API_KEY)
        editor.remove(prefKey)
        editor.apply()
    }

    fun getPrimaryApiToken(): String? {
        val prefKey = context.getString(R.string.PREF_primary_API_KEY)
        return pref.getString(prefKey, null)
    }

    fun registerPrimaryApiToken(token: String) {
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_primary_API_KEY)
        editor.putString(prefKey, token)
        editor.apply()
    }

    fun deleteD2DApiToken() {
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_primary_API_KEY)
        editor.remove(prefKey)
        editor.apply()
    }

    fun registerLoginCred(userName: String, password: String) {
        val editor = pref.edit()
        val prefUserKey = context.getString(R.string.PREF_rem_me_uname)
        val prefUserPwdKey = context.getString(R.string.PREF_rem_me_pwd)
        editor.putString(prefUserKey, userName)
        editor.putString(prefUserPwdKey, password)
        editor.apply()
    }

    fun deleteLoginCred() {
        val editor = pref.edit()
        val prefUserKey = context.getString(R.string.PREF_rem_me_uname)
        val prefUserPwdKey = context.getString(R.string.PREF_rem_me_pwd)
        editor.remove(prefUserKey)
        editor.remove(prefUserPwdKey)
        editor.apply()
    }

    fun getRememberedUserName(): String? {
        val key = context.getString(R.string.PREF_rem_me_uname)
        return pref.getString(key, null)
    }

    fun getRememberedPassword(): String? {
        val key = context.getString(R.string.PREF_rem_me_pwd)
        return pref.getString(key, null)
    }

    fun saveLocationRecord(locationRecord: LocationRecord) {
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_location_record_entry)
        val locationRecordJson = Gson().toJson(locationRecord)
        editor.putString(prefKey, locationRecordJson)
        editor.apply()
    }

    fun getLocationRecord(): LocationRecord? {
        val prefKey = context.getString(R.string.PREF_location_record_entry)
        val json = pref.getString(prefKey, null)
        return Gson().fromJson(json, LocationRecord::class.java)
    }

    fun setLastSyncedTimeStamp(lastSaved: Long) {
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_full_load_pull_progress)
        editor.putLong(prefKey, lastSaved)
        editor.apply()
    }

    fun getLastSyncedTimeStamp(): Long {
        val prefKey = context.getString(R.string.PREF_full_load_pull_progress)
        return pref.getLong(prefKey, 1603132200000)
    }

    fun saveSetLanguage(language: Languages) {
        val key = context.getString(R.string.PREF_current_saved_language)
        val editor = pref.edit()
        editor.putString(key, language.symbol)
        editor.apply()
    }

    fun getCurrentLanguage(): Languages {
        val key = context.getString(R.string.PREF_current_saved_language)
        return when (pref.getString(key, null)) {
            Languages.ASSAMESE.symbol -> Languages.ASSAMESE
            Languages.HINDI.symbol -> Languages.HINDI
            Languages.ENGLISH.symbol -> Languages.ENGLISH
            else -> Languages.ENGLISH
        }
    }
}