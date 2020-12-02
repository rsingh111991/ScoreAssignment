package com.thescore.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thescore.persistence.entity.Players
import com.thescore.persistence.entity.Teams
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface PlayersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPlayers(listOfTeams: List<Players>)

    @Query("SELECT * FROM Players WHERE teamID = :teamId")
    fun loadAllByIds(teamId: Int): Single<Players>

}