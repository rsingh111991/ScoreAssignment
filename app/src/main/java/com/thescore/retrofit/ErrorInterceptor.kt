package com.thescore.retrofit

import androidx.annotation.NonNull
import com.thescore.retrofit.connection.IInternetObserver
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject


class ErrorInterceptor @Inject constructor() :
    Interceptor {
    companion object {
        const val CONTENT_TYPE = "Content-Type"
        const val ACCEPT = "Accept"
        const val TYPE_HTML = "text/html"
        const val TYPE_JSON = "application/json"

    }
    override fun intercept(@NonNull chain: Interceptor.Chain): Response {
            try {
                val request = chain.request()
                val response = chain.proceed(request)
                if (response.isSuccessful) {
                    val requestAccept = request.header(ACCEPT)
                    if (TYPE_HTML == requestAccept) {
                        return response
                    }
                    val responseContentType = response.header(CONTENT_TYPE)
                    if (responseContentType != null && !responseContentType.contains(TYPE_JSON)) {
                        if (response.peekBody(2048)!!.string().toLowerCase(Locale.getDefault())
                                .contains("request rejected")
                        ) {
                            throw RuntimeException("Service is unavailable.")
                        }
                    }
                } else if (response.code() == 500) {
                    throw RuntimeException()
                }
                return response
            } catch (exception: SocketTimeoutException) {
                exception.printStackTrace()
                throw RuntimeException()
            }

        }


}