package com.thescore.teams.teamdetail.sort

import com.thescore.retrofit.PlayersElements
import java.util.*
import kotlin.Comparator
//Data class to load player user interface
data class UiPlayer(
    val recyclerViewItemType: RecyclerViewItemType,
    val playersElements: PlayersElements?,
    val initials: String? = null,
    val currentPosition: String? = null
)