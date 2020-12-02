package com.thescore.retrofit

import com.thescore.persistence.entity.Teams
import com.thescore.teams.teamslist.sort.TeamListOrder
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface ScoreWebApi {
    @Headers("Content-Type: application/json")
    @GET("input.json")
    fun getScoreTeamData(): Single<Response<List<Teams>>>

    class Factory {
        companion object{
            private const val BASE_URL = "https://raw.githubusercontent.com/scoremedia/nba-team-viewer/master/"
            fun create(errorInterceptor: ErrorInterceptor): ScoreWebApi{
                val client = OkHttpClient.Builder()
                client.addInterceptor(errorInterceptor)

               return Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                   .addConverterFactory(GsonConverterFactory.create())
                   .client(client.build())
                   .build()
                   .create(ScoreWebApi::class.java)
            }
        }
    }
}