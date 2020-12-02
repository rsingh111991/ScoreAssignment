package com.thescore.teams.teamdetail

import androidx.lifecycle.ViewModel
import com.thescore.retrofit.PlayersElements
import com.thescore.retrofit.Resource
import com.thescore.retrofit.Status
import com.thescore.teams.teamdetail.sort.PlayerMapper
import com.thescore.teams.uimodel.PlayerActionableItem
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.Observable
import javax.inject.Inject

@ActivityScoped
class TeamDetailViewModel @Inject constructor(private val teamDetailRepository: TeamDetailRepository, private val playerMapper: PlayerMapper): ViewModel() {
    //get player for a given ID
    fun getPlayers(teamId: Int): Observable<Resource<ArrayList<PlayerActionableItem>>> {
         return teamDetailRepository.getPlayersForTeam(teamId)
            .map { players  ->
                val playerActionableItem = ArrayList<PlayerActionableItem>()

                    playerActionableItem.add(getSortedPlayers(players.players, players.teamName))

                if(playerActionableItem.isEmpty()){
                    Resource.newResource(Status.EMPTY,ArrayList(), Throwable())
                }else{
                    Resource.newResource(Status.SUCCESS,playerActionableItem,Throwable())
                }
            }.toObservable()

    }
    //Sort players with respects to player position
    private fun getSortedPlayers(playerElement: List<PlayersElements>, teamName: String?): PlayerActionableItem {
        return PlayerActionableItem(playerMapper.apply(playerElement),
        teamName ?: kotlin.run { "" })
    }
}



