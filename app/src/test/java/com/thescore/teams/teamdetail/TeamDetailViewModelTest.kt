package com.thescore.teams.teamdetail

import com.thescore.mocks.FakeTeams
import com.thescore.persistence.entity.Players
import com.thescore.teams.teamdetail.sort.PlayerMapper
import io.mockk.every
import io.mockk.mockkClass
import io.reactivex.Single
import org.junit.Before
import org.junit.Test


class TeamDetailViewModelTest{

    private lateinit var players: Players
    private lateinit var teamDetailRepository: TeamDetailRepository
    private lateinit var teamDetailViewModel: TeamDetailViewModel
    private lateinit var playerMapper: PlayerMapper
    @Before
    fun setUp() {
        players = FakeTeams.getPlayersFromResource()!!
        teamDetailRepository = mockkClass(TeamDetailRepository::class)
        playerMapper = mockkClass(PlayerMapper::class)
        teamDetailViewModel = TeamDetailViewModel(teamDetailRepository, playerMapper)
    }

    @Test
    fun `get Players success`() {
        /**
         * GIVEN the teams has players listed
         */
        every { teamDetailRepository.getPlayersForTeam(1) } returns
                (Single.just(players))
        /**
         * WHEN players are fetched and sorted according to player position to group together
         */
        every { playerMapper.apply(players.players) } returns
                (PlayerMapper().apply(players.players))

        /**
         * THEN we get sorted list
         */
        teamDetailViewModel.getPlayers(1)
            .test()
            .await()
            .assertValue {
                it.data?.size!! > 0
            }
    }
}