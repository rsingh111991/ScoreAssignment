package com.thescore.teams.teamslist

import com.thescore.mocks.FakeTeams
import com.thescore.persistence.entity.Teams
import com.thescore.retrofit.Resource
import com.thescore.retrofit.Status
import com.thescore.teams.teamslist.sort.TeamListOrder
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.unmockkAll
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ScoreViewModelTest {

    private lateinit var teamsList: List<Teams>
    lateinit var teamListRepository: TeamListRepository
    lateinit var scoreViewModel: ScoreViewModel
    @Before
    fun setUp() {
        teamsList = FakeTeams.getTeamResourceFromResource()!!.asList()
        teamListRepository = mockkClass(TeamListRepository::class)
        scoreViewModel = ScoreViewModel(teamListRepository)
    }

    @Test
    fun `observe TeamSubject Success with valid data`() {
        /**
         * GIVEN Internet is connected
         */
        val orderType = TeamListOrder.HighestWins()
        /**
         * WHEN we call service to fetch data
         */
        every { teamListRepository.getOrderedTeamList(orderType) } returns
                ( Observable.just(Resource.newResource(Status.SUCCESS,teamsList, null)).toFlowable(BackpressureStrategy.LATEST))

        /**
         * THEN If the data is not available locally it should be fetched from server
         */
        scoreViewModel.observeTeamSubject(orderType)
            ?.test()
            ?.await()
            ?.assertValue {
                it.data?.size == 2
            }
    }

    @Test
    fun `observe TeamSubject with empty list data`() {
        /**
         * GIVEN Internet is connected
         */
        val orderType = TeamListOrder.HighestWins()
        val teams = listOf<Teams>()
        /**
         * WHEN The teams list is empty
         */
        every { teamListRepository.getOrderedTeamList(orderType) } returns
                ( Observable.just(Resource.newResource(Status.SUCCESS,teams, Throwable("Empty Data"))).toFlowable(BackpressureStrategy.LATEST))

        /**
         * Call should return Status. ERROR and list size 0
         */
        scoreViewModel.observeTeamSubject(orderType)
            ?.test()
            ?.await()
            ?.assertValue {
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