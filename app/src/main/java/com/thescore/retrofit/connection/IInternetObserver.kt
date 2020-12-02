package com.thescore.retrofit.connection


interface IInternetObserver {
    fun observerInternetConnection(): io.reactivex.Observable<Boolean>
    fun isNetworkConnected(): Boolean
}