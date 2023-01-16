package org.piramalswasthya.sakhi.database.shared_preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.piramalswasthya.sakhi.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDao @Inject constructor( @ApplicationContext private val context: Context) {

    private val pref = PreferenceManager.getInstance(context)

    fun getApiToken() : String?{
        val prefKey = context.getString(R.string.PREF_API_KEY)
        return pref.getString(prefKey,null)
    }

    fun registerApiToken(token : String){
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_API_KEY)
        editor.putString(prefKey,token)
        editor.apply()
    }

    fun deleteApiToken(){
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_API_KEY)
        editor.remove(prefKey)
        editor.apply()
    }


}