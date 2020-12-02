package com.thescore.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thescore.persistence.entity.Teams
import io.reactivex.Flowable

@Dao
interface TeamsDao {

    @Query("SELECT * FROM Teams ORDER BY CASE :teamListOrder  WHEN 'wins' THEN wins  WHEN 'losses' THEN losses END DESC, CASE WHEN :teamListOrder = 'Alphabetically' THEN full_name END")
    fun getAll(teamListOrder: String): Flowable<List<Teams>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTeams(listOfTeams: List<Teams>)
}