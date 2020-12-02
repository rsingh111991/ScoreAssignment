package com.thescore.retrofit

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
// Class to parse players from server
data class PlayersElements(
    @SerializedName("id")
    @ColumnInfo(name = "id", index = true) val playerId: Long,

    @SerializedName("first_name")
    val firstName: String?, //(Player's first name)

    @SerializedName("last_name")
    val lastName: String?, //(Player's last name)

    val position: String?, //(Player's position)

    @SerializedName("number")
    val jerseyNumber: Int, //(Player's jersey number)
)
