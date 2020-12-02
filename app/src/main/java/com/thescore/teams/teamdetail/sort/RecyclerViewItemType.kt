package com.thescore.teams.teamdetail.sort

sealed class RecyclerViewItemType(val itemType: String){
    class Header: RecyclerViewItemType("header")
    class Player: RecyclerViewItemType("player")
}
