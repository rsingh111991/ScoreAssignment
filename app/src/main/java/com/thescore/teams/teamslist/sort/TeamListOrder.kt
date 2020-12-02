package com.thescore.teams.teamslist.sort

sealed class TeamListOrder(val order: String) {
    class Alphabetically: TeamListOrder("Alphabetically")
    class HighestWins: TeamListOrder("wins")
    class MostLost: TeamListOrder("losses")
}