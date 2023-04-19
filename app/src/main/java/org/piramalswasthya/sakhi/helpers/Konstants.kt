package org.piramalswasthya.sakhi.helpers

object Konstants {



    const val editTextHintLimit: Byte = 50
    const val benIdCapacity: Int = 100
    const val benIdWorkerTriggerLimit = 90

    //Note: min and max both are inclusive for age ranges
    const val minAgeForEligibleCouple: Int = 15
    const val maxAgeForEligibleCouple: Int = 49
    const val minAgeForNcd: Int = 30
    const val minAgeForReproductiveAge: Int = 15
    const val maxAgeForReproductiveAge: Int = 49
    const val maxAgeForInfant: Int = 1
    const val minAgeForChild: Int = 2
    const val maxAgeForChild: Int = 5
    const val minAgeForAdolescent: Int = 6
    const val maxAgeForAdolescent: Int = 14
    const val maxAgeForCdr: Int = 14


    //HBNC
    const val hbncCardDay = 0
    const val hbncPart1Day = -1
    const val hbncPart2Day = -2


    const val defaultTimeStamp = 1603132200000L
}