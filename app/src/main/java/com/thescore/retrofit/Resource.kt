package com.thescore.retrofit

import java.util.*
//Generic resource class to send updates to UI
class Resource<T> private constructor(val status: Status, var data: T? = null, val throwable: Throwable? = null) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(status = Status.SUCCESS, data = data)
        }

        fun <T> error(throwable: Throwable?, data: T?): Resource<T> {
            return Resource(Status.ERROR, data = data, throwable = throwable)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(status = Status.LOADING, data = data)
        }

        fun <T> newResource(status: Status, data: T?, throwable: Throwable?): Resource<T> {
            return Resource(status = status, data = data, throwable = throwable)
        }
    }

}