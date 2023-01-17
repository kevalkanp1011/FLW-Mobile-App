package org.piramalswasthya.sakhi.configuration

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

object FormEditTextDefaultInputFilter  : InputFilter{

    private val regex = Pattern.compile("^[A-Za-z ]*$")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val matcher = regex.matcher(source)
        return if (matcher.find()) {
            null
        } else {
            ""
        }
    }
}