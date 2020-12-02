package com.thescore.teams.teamslist

import androidx.lifecycle.ViewModel
import com.thescore.persistence.entity.Teams
import com.thescore.retrofit.Resource
import com.thescore.retrofit.Status
import com.thescore.teams.teamslist.sort.TeamListOrder
import com.thescore.teams.uimodel.ActionableItem
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@ActivityScoped
class ScoreViewModel @Inject  constructor(private val teamListRepository: TeamListRepository) : ViewModel() {
    var navigator: TeamListNavigator? = null

    //Actionable items to handle click events
    private fun getActionableTeam(team: Teams): ActionableItem {
        return ActionableItem(team) {
            if(team.playersCount>0) navigator?.navigateToTeamDetailActivity(team.id)
            else navigator?.showMessage()
        }
    }
    //fetch team from database
    fun observeTeamSubject(teamListOrder: TeamListOrder): Observable<Resource<MutableList<ActionableItem>>>? {
        return teamListRepository.getOrderedTeamList(teamListOrder)
            .subscribeOn(Schedulers.io())
            .map { resourceList ->
                val actionableTeamArrayList = mutableListOf<ActionableItem>()
                if(resourceList.data!=null) {
                    for (team in resourceList.data!!) {
                        actionableTeamArrayList.add(getActionableTeam(team))
                    }
                }
                if(actionableTeamArrayList.isEmpty()){
                    Resource.newResource(Status.EMPTY,ArrayList(), Throwable("Empty Data"))
                }else{
                    Resource.newResource(resourceList.status,actionableTeamArrayList,resourceList.throwable)
                }

            }.toObservable()
    }
}