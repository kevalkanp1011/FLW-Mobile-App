package org.piramalswasthya.sakhi.repositories

import javax.inject.Inject

class AbhaIdRepo {
    fun getAccessToken(callback: AccessTokenCallback) {

    }
}

interface AccessTokenCallback {
    fun onSuccess(token: String)
    fun onError(error: String)
}