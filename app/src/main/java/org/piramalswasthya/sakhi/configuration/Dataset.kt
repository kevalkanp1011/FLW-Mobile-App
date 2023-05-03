package org.piramalswasthya.sakhi.configuration

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.FormElement
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


/**
 * Base class to be extended to use as a sandwich between viewModel and repository objects.
 * @see org.piramalswasthya.sakhi.adapters.FormInputAdapter
 */
abstract class Dataset(context: Context, currentLanguage: Languages) {

    /**
     * Resource object of currently selected language. To be used to get language specific strings from strings.xml.
     */
    protected var resources: Resources

    /**
     * Helper function to get resource instance chosen language.
     */
    private fun getLocalizedResources(context: Context, currentLanguage: Languages): Resources {
        val desiredLocale = Locale(currentLanguage.symbol)
        var conf = context.resources.configuration
        conf = Configuration(conf)
        conf.setLocale(desiredLocale)
        val localizedContext: Context = context.createConfigurationContext(conf)
        return localizedContext.resources
    }

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

    private val _alertErrorMessageFlow = MutableStateFlow<String?>(null)
    val alertErrorMessageFlow = _alertErrorMessageFlow.asStateFlow()

    suspend fun resetErrorMessageFlow() {
        _alertErrorMessageFlow.emit(null)
    }

    protected fun FormElement.getPosition(): Int {
        return value?.let { entries?.indexOf(it)?.plus(1) } ?: 0
    }

    protected fun FormElement.getStringFromPosition(position: Int): String? {
        return if (position == 0) null else entries?.get(position - 1)
    }

    protected abstract suspend fun handleListOnValueChanged(formId: Int, index: Int): Int

    abstract fun mapValues(cacheModel: FormDataModel, pageNumber: Int = 0)

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
        _alertErrorMessageFlow.emit(resources.getString(errorMessage))
    }

    protected fun validateEmptyOnEditText(formElement: FormElement): Int {
        if (formElement.required) {
            if (formElement.value.isNullOrEmpty()) formElement.errorText =
                "This Field Cannot Be Empty"
            else {
                formElement.errorText = null
            }
        }
        return -1
    }

    private fun String.isAllUppercaseOrSpace() =
        takeIf { it.isNotEmpty() }?.toCharArray()?.all { it.isUpperCase() || it.isWhitespace() } ?: false


    private fun String.isAllAlphabetsAndSpace() =
        takeIf { it.isNotEmpty() }?.toCharArray()?.all { it.isWhitespace() || it.isLetter() }
            ?: false

    protected fun validateAllCapsOrSpaceOnEditText(formElement: FormElement): Int {
        if (formElement.allCaps) {
            formElement.value?.takeIf { it.isNotEmpty() }?.isAllUppercaseOrSpace()?.let {
                Timber.d("Is ok : $it")
                formElement.errorText = if (it) null
                else "Only Alphabets in upper case allowed!"
            } ?: run {
                if (!formElement.required) formElement.errorText = null
            }
        }
        return -1
    }

    protected fun validateAllAlphabetsSpaceOnEditText(formElement: FormElement): Int {
        formElement.value?.takeIf { it.isNotEmpty() }?.isAllAlphabetsAndSpace()?.let {
            if (it) formElement.errorText = null
            else formElement.errorText = "Only Alphabets and whitespace allowed!"
        }
        return -1
    }


    protected fun validateMobileNumberOnEditText(formElement: FormElement): Int {
        formElement.errorText = formElement.value?.takeIf { it.isNotEmpty() }?.toLong()?.let {
            if (it < 6_000_000_000L) "Invalid Mobile Number" else null
        }
        return -1
    }

    init {
        resources = getLocalizedResources(context, currentLanguage)
    }

}