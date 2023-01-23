package org.piramalswasthya.sakhi.database.shared_preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.piramalswasthya.sakhi.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDao @Inject constructor( @ApplicationContext private val context: Context) {

    private val pref = PreferenceManager.getInstance(context)

    fun getD2DApiToken() : String?{
        val prefKey = context.getString(R.string.PREF_D2D_API_KEY)
        return pref.getString(prefKey,null)
    }

    fun registerD2DApiToken(token : String){
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_D2D_API_KEY)
        editor.putString(prefKey,token)
        editor.apply()
    }

    fun deletePrimaryApiToken(){
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_D2D_API_KEY)
        editor.remove(prefKey)
        editor.apply()
    }

    fun getPrimaryApiToken() : String?{
        val prefKey = context.getString(R.string.PREF_primary_API_KEY)
        return pref.getString(prefKey,null)
    }

    fun registerPrimaryApiToken(token : String){
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_primary_API_KEY)
        editor.putString(prefKey,token)
        editor.apply()
    }

    fun deleteD2DApiToken(){
        val editor = pref.edit()
        val prefKey = context.getString(R.string.PREF_primary_API_KEY)
        editor.remove(prefKey)
        editor.apply()
    }


}