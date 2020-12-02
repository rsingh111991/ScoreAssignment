package com.thescore.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thescore.persistence.converter.Converter
import com.thescore.persistence.dao.PlayersDao
import com.thescore.persistence.dao.TeamsDao
import com.thescore.persistence.entity.Players
import com.thescore.persistence.entity.Teams

@Database(entities = [Teams::class, Players::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ScoreDatabase: RoomDatabase() {

    companion object{
        const val DATABASE_NAME = "room_score_db"
    }
    abstract fun teamsDao(): TeamsDao

    abstract fun playersDao(): PlayersDao
}