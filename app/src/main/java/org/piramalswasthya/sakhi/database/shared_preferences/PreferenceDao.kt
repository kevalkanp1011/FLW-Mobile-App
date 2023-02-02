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

    fun registerLoginCred(userName : String, password : String){
        val editor = pref.edit()
        val prefUserKey = context.getString(R.string.PREF_rem_me_uname)
        val prefUserPwdKey = context.getString(R.string.PREF_rem_me_pwd)
        editor.putString(prefUserKey,userName)
        editor.putString(prefUserPwdKey,password)
        editor.apply()
    }
    fun deleteLoginCred(){
        val editor = pref.edit()
        val prefUserKey = context.getString(R.string.PREF_rem_me_uname)
        val prefUserPwdKey = context.getString(R.string.PREF_rem_me_pwd)
        editor.remove(prefUserKey)
        editor.remove(prefUserPwdKey)
        editor.apply()
    }

    fun getRememberedUserName() : String?{
        val key =  context.getString(R.string.PREF_rem_me_uname)
        return pref.getString(key,null)
    }
    fun getRememberedPassword() : String?{
        val key = context.getString(R.string.PREF_rem_me_pwd)
        return pref.getString(key,null)
    }


}