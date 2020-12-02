package com.thescore.teams.teamslist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.thescore.R
import com.thescore.teams.teamdetail.TeamDetailActivity
import java.lang.ref.WeakReference

//Class handles navigation and activity methods
open class TeamListNavigator(activity: AppCompatActivity) {
    private val activityWeakReference: WeakReference<Activity> = WeakReference(activity)

    fun navigateToTeamDetailActivity(teamId: Int){
        if(activityWeakReference.get() != null){
            activityWeakReference.get()!!.startActivity(TeamDetailActivity.startIntent(activityWeakReference.get()!!.applicationContext, teamId))
        }
    }

    fun showMessage(){
        if(activityWeakReference.get() != null){
            Snackbar.make(activityWeakReference.get()!!.window.decorView.findViewById(android.R.id.content), R.string.no_player_found, Snackbar.LENGTH_LONG)
                .show()
        }
    }
}