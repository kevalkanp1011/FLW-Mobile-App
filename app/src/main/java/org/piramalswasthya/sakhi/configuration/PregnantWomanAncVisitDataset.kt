package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.helpers.Languages

class PregnantWomanAncVisitDataset(
    context: Context,
    currentLanguage: Languages
) : Dataset(context, currentLanguage) {


    override suspend fun handleListOnValueChanged(formId: Int, index: Int): Int {
        TODO("Not yet implemented")
    }

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {
        TODO("Not yet implemented")
    }
}