package com.thescore.retrofit.connection

interface RemoteSettings {
    fun fetchFromRemote(): Boolean
    fun updateLastFetchTime()
}