package org.piramalswasthya.sakhi.helpers

import org.piramalswasthya.sakhi.model.BenBasicDomain

fun filterBenList(list: List<BenBasicDomain> ,text: String): List<BenBasicDomain> {
    if (text == "")
       return list
    else{
        val filterText = text.lowercase()
        return list.filter {
            it.hhId.toString().lowercase().contains(filterText) ||
                    it.benId.toString().lowercase().contains(filterText) ||
                    it.regDate.lowercase().contains((filterText)) ||
                    it.age.lowercase().contains(filterText) ||
                    it.benName.lowercase().contains(filterText) ||
                    it.familyHeadName.lowercase().contains(filterText) ||
                    it.benSurname?.lowercase()?.contains(filterText) ?: false ||
                    it.typeOfList.lowercase().contains(filterText) ||
                    it.mobileNo.lowercase().contains(filterText) ||
                    it.gender.lowercase().contains(filterText) ||
                    it.fatherName?.lowercase()?.contains(filterText) ?: false
        }
    }
}