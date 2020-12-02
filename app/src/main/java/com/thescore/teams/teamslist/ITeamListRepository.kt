package com.thescore.teams.teamslist

import com.thescore.persistence.entity.Teams
import com.thescore.retrofit.Resource
import com.thescore.teams.teamslist.sort.TeamListOrder
import io.reactivex.Flowable

interface ITeamListRepository {
    fun getOrderedTeamList(teamListOrder: TeamListOrder): Flowable<Resource<List<Teams>>>
}