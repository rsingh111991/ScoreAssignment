package com.thescore.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.thescore.persistence.dao.PlayersDao
import com.thescore.persistence.ScoreDatabase
import com.thescore.persistence.dao.TeamsDao
import com.thescore.retrofit.ErrorInterceptor
import com.thescore.retrofit.ScoreWebApi
import com.thescore.retrofit.connection.IInternetObserver
import com.thescore.retrofit.connection.InternetObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DBModule {

    @Provides
    fun provideTeamsDao(scoreDatabase: ScoreDatabase): TeamsDao {
        return scoreDatabase.teamsDao()
    }

    @Provides
    fun providePlayersDao(scoreDatabase: ScoreDatabase): PlayersDao {
        return scoreDatabase.playersDao()
    }

    @Provides
    @Singleton
    fun provideScoreDatabase(@ApplicationContext context: Context): ScoreDatabase {
        return Room.databaseBuilder(context,ScoreDatabase::class.java, ScoreDatabase.DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideScoreWebApi(errorInterceptor: ErrorInterceptor): ScoreWebApi {
        return ScoreWebApi.Factory.create(errorInterceptor)
    }

    @Provides
    fun provideInternetObserver(@ApplicationContext context: Context): IInternetObserver {
        return InternetObserver(context)
    }

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}