package com.thescore.teams.teamdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.thescore.BaseActivity
import com.thescore.R
import com.thescore.databinding.ActivityTeamDetailBinding
import com.thescore.retrofit.Resource
import com.thescore.retrofit.Status
import com.thescore.teams.uimodel.PlayerActionableItem
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TeamDetailActivity : BaseActivity() {

    companion object {
        private const val TEAM_ID = "teamId"
        fun startIntent(context: Context, teamId: Int): Intent {
            val launchingIntent = Intent(context, TeamDetailActivity::class.java)
            launchingIntent.putExtra(TEAM_ID, teamId)
            return launchingIntent
        }
    }

    private lateinit var playerRoosterAdapter: PlayerRoosterAdapter
    private var teamID: Int = 0

    @Inject
    lateinit var teamDetailViewModel: TeamDetailViewModel
    private val compositeDisposable = CompositeDisposable()
    private lateinit var binding: ActivityTeamDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)
        teamID = intent.getIntExtra(TEAM_ID, 0)
        binding = ActivityTeamDetailBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        unableBackButton()
        setContentView(binding.root)
        setUpLayoutManager()
    }
    private fun setUpLayoutManager() {
        binding.teamRoosterList.layoutManager = LinearLayoutManager(this)
    }
    override fun onStart() {
        super.onStart()
        compositeDisposable.add(
            teamDetailViewModel.getPlayers(teamID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    handlePlayersData(it)
                }, { throwable ->
                    Timber.e(throwable.localizedMessage)
                    showErrorMessage(getString(R.string.no_data_found))
                })
        )
    }

    private fun handlePlayersData(it: Resource<ArrayList<PlayerActionableItem>>?) {
        if (it?.status == Status.SUCCESS) {
            if (it.data.isNullOrEmpty().not()) {
                val actionableItem = it.data!![0]
                binding.teamRoosterList.isVisible = true
                binding.noDataFoundDetailScreen.root.isVisible = false
                playerRoosterAdapter = PlayerRoosterAdapter(actionableItem.uiPlayerList)
                binding.teamRoosterList.adapter = playerRoosterAdapter
                binding.teamName.text = actionableItem.teamName
            } else {
                showErrorMessage(getString(R.string.no_data_found))
            }
        } else {
            showErrorMessage(getString(R.string.no_data_found))
        }
    }

    override fun showErrorMessage(errorTitle: String) {
        binding.noDataFoundDetailScreen.root.isVisible = true
        binding.noDataFoundDetailScreen.errorTitle.text = errorTitle
        binding.noDataFoundDetailScreen.errorMessage.text = getString(R.string.please_try_again_later)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}