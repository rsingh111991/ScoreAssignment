package com.thescore.mocks

import com.thescore.persistence.entity.Teams
import com.thescore.retrofit.ScoreWebApi
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

class FakeApi(private var failureMode: Boolean, private val teams: List<Teams>?) : ScoreWebApi {
    override fun getScoreTeamData(): Single<Response<List<Teams>>> {

        return if(failureMode)Single.just(Response.error(400, ResponseBody.create(
            MediaType.parse("application/json"),
            "{\"message\":[\"Error\"]}"
        )))
        else Single.just(Response.success(teams))
    }

    fun updateFailureMode(failureMode: Boolean) {
    this.failureMode = failureMode
    }
}