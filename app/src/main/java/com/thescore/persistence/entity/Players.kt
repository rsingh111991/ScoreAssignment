package com.thescore.persistence.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.thescore.persistence.converter.Converter
import com.thescore.retrofit.PlayersElements

@Entity
data class Players(
    @TypeConverters(Converter::class)
    @ColumnInfo(name = "players", index = true) val  players: List<PlayersElements> = listOf(),

    @ColumnInfo(name = "teamName")val teamName: String?, //(Player's position)

    @PrimaryKey
    @ColumnInfo(name = "teamID")val teamId: Int?, //(Player's position)

)