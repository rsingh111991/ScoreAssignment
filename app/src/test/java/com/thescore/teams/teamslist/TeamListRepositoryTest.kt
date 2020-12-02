package com.thescore.teams.teamslist

import android.content.SharedPreferences
import com.thescore.mocks.FakeApi
import com.thescore.mocks.FakeTeams
import com.thescore.persistence.ScoreDatabase
import com.thescore.persistence.dao.PlayersDao
import com.thescore.persistence.dao.TeamsDao
import com.thescore.persistence.entity.Teams
import com.thescore.retrofit.connection.IInternetObserver
import com.thescore.teams.teamslist.sort.TeamListOrder
import io.mockk.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test

class TeamListRepositoryTest {


    private lateinit var teamsList: List<Teams>
    lateinit var teamListRepository: TeamListRepository
    lateinit var scoreWebApi: FakeApi
    lateinit var teamsDao: TeamsDao
    lateinit var playersDao: PlayersDao
    lateinit var scoreDatabase: ScoreDatabase
    lateinit var sharedPreferences: SharedPreferences
    var isInternetConnected = true
    var isFailureMode = false

    @Before
    fun setUp() {
        teamsList = FakeTeams.getTeamResourceFromResource()!!.asList()
        scoreWebApi = FakeApi(isFailureMode, teamsList)
        teamsDao = mockkClass(TeamsDao::class)
        playersDao = mockkClass(PlayersDao::class)
        scoreDatabase = mockkClass(ScoreDatabase::class)
        sharedPreferences = mockkClass(SharedPreferences::class)
        every { scoreDatabase.teamsDao() } returns (teamsDao)
        every { scoreDatabase.playersDao() } returns (playersDao)
         }

    private fun getInternetObserver(): IInternetObserver {
        return object : IInternetObserver{
            override fun observerInternetConnection(): Observable<Boolean> {
                return Observable.just(isInternetConnected)
            }

            override fun isNetworkConnected(): Boolean {
                return isInternetConnected
            }
        }
    }

    @Test
    fun `getTeams from server with internet connected`() {
        /**
         * GIVEN Internet is connected
         */
        val orderType = TeamListOrder.Alphabetically()
        every { sharedPreferences.getLong(any(),0) } returns (0)
        isInternetConnected = true
        scoreWebApi.updateFailureMode(false)
        teamListRepository = TeamListRepository(scoreWebApi,scoreDatabase,teamsDao,playersDao,getInternetObserver() ,sharedPreferences)

        /**
         * WHEN call is made to get teams
         */
        every { teamsDao.getAll(orderType.order) } returns ( Observable.just(teamsList).toFlowable(BackpressureStrategy.LATEST))

        /**
         * THEN fetched from server and stored locally
         * OR fetch locally in selected order
         */
        teamListRepository.getOrderedTeamList(orderType)
            .test()
            .assertValue{
                it.data?.size == 2
            }
    }


    @Test
    fun `getTeams from server with internet off`() {
        /**
         * GIVEN Internet is not connected
         */
        val orderType = TeamListOrder.Alphabetically()
        isInternetConnected = false
        scoreWebApi.updateFailureMode(true)
        teamListRepository = TeamListRepository(scoreWebApi,scoreDatabase,teamsDao,playersDao,getInternetObserver() ,sharedPreferences)

        /**
         * WHEN we try to get teams
         */
        every { sharedPreferences.getLong(any(),0) } returns (0)
        val teams = listOf<Teams>()
        every { teamsDao.getAll(orderType.order) } returns ( Observable.just(teams).toFlowable(BackpressureStrategy.LATEST))

        /**
         * Call should return Status. ERROR and list size 0
         */
        teamListRepository.getOrderedTeamList(orderType)
            .test(1)
            .assertValue{
                it.data?.size == 0
            }
    }


    @After
    fun tearDown() {
        clearAllMocks(true)
    }

    @AfterClass
    fun closeMockks(){
        unmockkAll()
    }
}