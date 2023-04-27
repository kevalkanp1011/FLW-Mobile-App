package org.piramalswasthya.sakhi.configuration

import android.content.res.Resources
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.piramalswasthya.sakhi.model.FormElement
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

abstract class Dataset(private val resource: Resources) {

    protected companion object {
        fun getLongFromDate(dateString: String?): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = dateString?.let { f.parse(it) }
            return date?.time ?: 0L
        }

        fun getDateFromLong(dateLong: Long): String? {
            if (dateLong == 0L) return null
            val cal = Calendar.getInstance()
            cal.timeInMillis = dateLong
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return f.format(cal.time)


        }
    }


    private val list = mutableListOf<FormElement>()

    private val _listFlow = MutableStateFlow<List<FormElement>>(emptyList())
    val listFlow = _listFlow.asStateFlow()

    private val _errorMessageFlow = MutableStateFlow<String?>(null)
    val errorMessageFlow = _errorMessageFlow.asStateFlow()

    suspend fun resetErrorMessageFlow() {
        _errorMessageFlow.emit(null)
    }

    protected fun FormElement.getPosition(): Int {
        return value?.let { entries?.indexOf(it)?.plus(1) } ?: 0
    }

    protected fun FormElement.getStringFromPosition(position: Int): String? {
        return if (position == 0) null else entries?.get(position - 1)
    }

    protected abstract suspend fun handleListOnValueChanged(formId: Int, index: Int): Int

    suspend fun updateList(formId: Int, index: Int) {
        val updateIndex = handleListOnValueChanged(formId, index)
        if (updateIndex != -1) {
            val newList = list.toMutableList()
//            if (updateUIForCurrentElement) {
//                Timber.d("Updating UI element ...")
//                newList[updateIndex] = list[updateIndex].cloneForm()
//                updateUIForCurrentElement = false
//            }
            Timber.d("Emitting ${newList}}")
            _listFlow.emit(newList)
        }
    }

    protected suspend fun setUpPage(mList: List<FormElement>) {
        list.clear()
        list.addAll(mList)
        _listFlow.emit(list.toMutableList())
    }


    protected fun triggerDependants(
        source: FormElement,
        passedIndex: Int,
        triggerIndex: Int,
        target: List<FormElement>,
        targetSideEffect: List<FormElement>? = null
    ): Int {
        return if (passedIndex == triggerIndex && !list.containsAll(target)) {
            val listIndex = list.indexOf(source)
            list.addAll(
                listIndex + 1, target
            )
            listIndex
        } else {
            val anyRemoved = list.removeAll(
                target
            )
            if (anyRemoved) {
                target.forEach {
                    it.value = null
                }
                targetSideEffect?.let { sideEffectList ->
                    list.removeAll(sideEffectList)
                    sideEffectList.forEach { it.value = null }
                }
                list.indexOf(source)
            } else -1
        }
    }

    protected fun triggerDependants(
        source: FormElement,
        passedIndex: Int,
        triggerIndex: Int,
        target: FormElement,
        targetSideEffect: List<FormElement>? = null
    ): Int {
        return if (passedIndex == triggerIndex && !list.contains(target)) {
            val listIndex = list.indexOf(source)
            list.add(
                listIndex + 1, target
            )

            listIndex
        } else {
            val anyRemoved = list.remove(
                target
            )
            if (anyRemoved) {
                target.value = null
                targetSideEffect?.let { sideEffectList ->
                    list.removeAll(sideEffectList)
                    sideEffectList.forEach { it.value = null }
                }
                list.indexOf(source)
            } else -1
        }
    }

    protected suspend fun emitAlertErrorMessage(
        @StringRes errorMessage: Int
    ) {
        _errorMessageFlow.emit(resource.getString(errorMessage))
    }

    protected fun isAMobileNumber(input: String?): String? {
        return input?.toLong()?.let {
            if (it >= 6_000_000_000L) null else "Invalid Mobile Number"
        }
    }

}