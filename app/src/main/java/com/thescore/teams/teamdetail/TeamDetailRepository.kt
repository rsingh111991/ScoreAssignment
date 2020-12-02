package com.thescore.teams.teamdetail

import com.thescore.persistence.dao.PlayersDao
import com.thescore.persistence.entity.Players
import io.reactivex.Single
import javax.inject.Inject

class TeamDetailRepository @Inject constructor(private val playersDao: PlayersDao) {

    fun getPlayersForTeam(teamId: Int): Single<Players> {
        return playersDao.loadAllByIds(teamId)
    }
}