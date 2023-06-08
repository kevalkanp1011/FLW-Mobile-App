package org.piramalswasthya.sakhi.helpers

import org.piramalswasthya.sakhi.model.BenBasicDomain
import org.piramalswasthya.sakhi.model.BenBasicDomainForForm

fun filterBenList(list: List<BenBasicDomain> ,text: String): List<BenBasicDomain> {
    if (text == "")
       return list
    else{
        val filterText = text.lowercase()
        return list.filter {
            filterForBen(it, filterText)
        }
    }
}
 fun filterForBen(
    ben: BenBasicDomain,
    filterText: String
) = ben.hhId.toString().lowercase().contains(filterText) ||
        ben.benId.toString().lowercase().contains(filterText) ||
        ben.regDate.lowercase().contains((filterText)) ||
        ben.age.lowercase().contains(filterText) ||
        ben.benName.lowercase().contains(filterText) ||
        ben.familyHeadName.lowercase().contains(filterText) ||
        ben.benSurname?.lowercase()?.contains(filterText) ?: false ||
//        ben.typeOfList.lowercase().contains(filterText) ||
        ben.mobileNo.lowercase().contains(filterText) ||
        ben.gender.lowercase().contains(filterText) ||
        ben.fatherName?.lowercase()?.contains(filterText) ?: false

@JvmName("filterBenList1")
fun filterBenList(list: List<BenBasicDomainForForm>, text: String): List<BenBasicDomainForForm> {
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
//                    it.typeOfList.lowercase().contains(filterText) ||
                    it.mobileNo.lowercase().contains(filterText) ||
                    it.gender.lowercase().contains(filterText) ||
                    it.fatherName?.lowercase()?.contains(filterText) ?: false
        }
    }
}