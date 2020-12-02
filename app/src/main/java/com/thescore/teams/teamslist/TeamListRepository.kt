package com.thescore.teams.teamslist

import android.content.SharedPreferences
import com.thescore.persistence.dao.PlayersDao
import com.thescore.persistence.ScoreDatabase
import com.thescore.persistence.dao.TeamsDao
import com.thescore.persistence.entity.Players
import com.thescore.persistence.entity.Teams
import com.thescore.retrofit.NetworkBoundResource
import com.thescore.retrofit.Resource
import com.thescore.retrofit.ScoreWebApi
import com.thescore.retrofit.connection.IInternetObserver
import com.thescore.retrofit.connection.RemoteSettings
import com.thescore.teams.teamslist.sort.TeamListOrder
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import retrofit2.Response
import javax.inject.Inject

class TeamListRepository @Inject constructor(var scoreWebApi: ScoreWebApi,
                                             val scoreDatabase: ScoreDatabase, var teamsDao: TeamsDao,
                                             var playersDao: PlayersDao,
                                             val internetObserver: IInternetObserver,
                                             private val preferences: SharedPreferences): ITeamListRepository, RemoteSettings {
companion object{
    const val FETCH_TIME ="fetch_time"
    const val UPDATE_THRESHOLD = 86400000 // 1 day wait to call service
}
    //fetch teams according to order
    override fun getOrderedTeamList(teamListOrder: TeamListOrder): Flowable<Resource<List<Teams>>>{
        return getTeams(teamListOrder)
    }

    private fun getTeams(teamListOrder: TeamListOrder): Flowable<Resource<List<Teams>>>{
        return Flowable.create({emitter ->
            object : NetworkBoundResource<List<Teams>, List<Teams>>(internetObserver, emitter){
                override fun getLocal(): Flowable<List<Teams>?> {
                    return teamsDao.getAll(teamListOrder.order)
                }

                override fun getFromRemote(): Single<Response<List<Teams>>> {
                   return scoreWebApi.getScoreTeamData()
                }

                override fun mapper(): Function<List<Teams>, List<Teams>> {
                    return Function { response ->
                        response
                    }
                }

                override fun saveCallResult(dataResponse: List<Teams>) {
                    scoreDatabase.runInTransaction {
                        val playersArrayList = ArrayList<Players>()
                        for( (index, team) in dataResponse.withIndex()){
                            if(team.players !=null) {
                                dataResponse[index].playersCount = team.players.size
                                val playersEntity = Players(team.players, team.teamFullName, team.id)
                                playersArrayList.add(playersEntity)
                            }

                        }
                        updateLastFetchTime()
                        playersDao.insertAllPlayers(playersArrayList)
                        teamsDao.insertAllTeams(dataResponse)
                    }
                }

                override fun shouldFetchFromRemote(): Boolean {
                    return fetchFromRemote()
                }

            }
        }, BackpressureStrategy.LATEST)
    }

    // Methods to get and store lastFetch time to prefes

    override fun fetchFromRemote(): Boolean {
        val oldTime = preferences.getLong(FETCH_TIME,0)
        return System.currentTimeMillis() - oldTime >= UPDATE_THRESHOLD
    }

    override fun updateLastFetchTime() {
        preferences.edit().putLong(FETCH_TIME, System.currentTimeMillis()).apply()
    }
}