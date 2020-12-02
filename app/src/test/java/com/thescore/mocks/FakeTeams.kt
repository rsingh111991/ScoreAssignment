package com.thescore.mocks

import com.google.gson.Gson
import com.thescore.persistence.entity.Players
import com.thescore.persistence.entity.Teams
import java.io.IOException

object FakeTeams {
    fun getTeamResourceFromResource(): Array<Teams>?{
        return try {
            val inputStream: String? = this.javaClass.getResource("/teams.json")?.readText(charset = Charsets.UTF_8)
            Gson().fromJson(inputStream, Array<Teams>::class.java)
        }catch (e: IOException){
            null
        }
    }

    fun getPlayersFromResource(): Players?{
        return try {
            val inputStream: String? = this.javaClass.getResource("/players.json")?.readText(charset = Charsets.UTF_8)
            Gson().fromJson(inputStream, Players::class.java)
        }catch (e: IOException){
            null
        }
    }
}