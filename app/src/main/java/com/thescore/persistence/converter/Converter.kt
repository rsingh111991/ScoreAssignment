package com.thescore.persistence.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thescore.persistence.entity.Players
import com.thescore.retrofit.PlayersElements
import java.lang.reflect.Type

class Converter {

    @TypeConverter
    fun fromPlayersToJson(players: List<PlayersElements>): String {
        return Gson().toJson(players)
    }

    @TypeConverter
    fun toPlayersResponse(playersJson: String): List<PlayersElements> {
        val listType  = object : TypeToken<List<PlayersElements>>(){}.type
        return Gson().fromJson(playersJson, listType)
    }
}