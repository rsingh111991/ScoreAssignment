package com.thescore.teams.teamdetail.sort

import com.thescore.retrofit.PlayersElements
import io.reactivex.functions.Function
import javax.inject.Inject

class PlayerMapper @Inject constructor(): Function<List<PlayersElements>, List<UiPlayer>> {
    override fun apply(players: List<PlayersElements>): List<UiPlayer> {
        val uiPlayer = ArrayList<UiPlayer>()

        val sortedList = players.sortedWith(compareBy { it.position })
        var currentPosition = sortedList[0].position
        uiPlayer.add(UiPlayer(RecyclerViewItemType.Header(), null, currentPosition = currentPosition ))
        for(player in sortedList){
            if(currentPosition != player.position){
                currentPosition = player.position
                uiPlayer.add(UiPlayer(RecyclerViewItemType.Header(), null, currentPosition = currentPosition))
            }
            uiPlayer.add(UiPlayer(RecyclerViewItemType.Player(), player, getInitialsForPlayer(player.firstName, player.lastName)))
            currentPosition = player.position
        }
        return uiPlayer
    }

    private fun getInitialsForPlayer(firstName: String?, lastName: String?): String {

        val builder = StringBuilder()
        if (firstName.isNullOrEmpty().not()) builder.append(firstName!!.substring(0,1))
        if (lastName.isNullOrEmpty().not()) {
            builder.append(lastName!!.substring(0,1))
        }
       return builder.toString()
    }
}