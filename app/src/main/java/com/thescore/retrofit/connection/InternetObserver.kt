package com.thescore.retrofit.connection

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class InternetObserver @Inject constructor(@ApplicationContext val context: Context): IInternetObserver {
    private var isNetworkConnected = false
    override fun observerInternetConnection(): io.reactivex.Observable<Boolean> {

        return ReactiveNetwork.observeNetworkConnectivity(context)
            .map { connectivity ->
                isNetworkConnected = connectivity.isAvailable
                isNetworkConnected
            }
            .subscribeOn(Schedulers.io())
        }
    override fun isNetworkConnected() = isNetworkConnected
}