package com.thescore.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.thescore.retrofit.PlayersElements

@Entity
data class Teams(
    @PrimaryKey val id: Int, //(Team's ID)
    @SerializedName("full_name")
    @ColumnInfo(name = "full_name") val teamFullName: String?, //(Team's name)
    val wins: Int, //(Team's wins)
    val losses: Int, //(Team's losses)
    var playersCount: Int = 0
){
    @Ignore val players: List<PlayersElements>? = null//(List of players on the team)
}
