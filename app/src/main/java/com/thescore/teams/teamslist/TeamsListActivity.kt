package com.thescore.teams.teamslist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.thescore.BaseActivity
import com.thescore.R
import com.thescore.databinding.ActivityMainBinding
import com.thescore.retrofit.Status
import com.thescore.teams.teamslist.sort.TeamListOrder
import com.thescore.teams.teamslist.sort.TeamListOrderBy
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TeamsListActivity : BaseActivity() {
    @Inject
    lateinit var scoreViewModel: ScoreViewModel
    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable = CompositeDisposable()
    private lateinit var teamItemRecyclerViewAdapter: TeamItemRecyclerViewAdapter
    private var orderSubject: BehaviorSubject<TeamListOrderBy> = BehaviorSubject.createDefault(TeamListOrderBy(TeamListOrder.Alphabetically()))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarInclude.toolbar)
        scoreViewModel.navigator = TeamListNavigator(this)
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(orderSubject
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .switchMap {
                scoreViewModel.observeTeamSubject(orderSubject.value!!.teamListOrder)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                if(it.status == Status.LOADING){
                    binding.progress.isVisible = true
                }else if(it.status == Status.SUCCESS){
                    if(it.data.isNullOrEmpty().not()){
                        binding.teamList.isVisible = true
                        binding.progress.isVisible = false
                        binding.noDataFound.root.isVisible = false
                        teamItemRecyclerViewAdapter = TeamItemRecyclerViewAdapter(it.data!!)
                        binding.teamList.adapter = teamItemRecyclerViewAdapter
                        binding.teamList.layoutManager = LinearLayoutManager(this)
                    }else{
                        showErrorMessage(getString(R.string.no_data_found))
                    }
                }else {
                    showErrorMessage(getString(R.string.no_data_found))
                }
            },{ throwable ->
                Timber.e(throwable.localizedMessage)
            }))
    }

     override fun showErrorMessage(errorTitle: String){
        binding.progress.isVisible = false
        binding.noDataFound.root.isVisible = true
        binding.noDataFound.errorTitle.text = errorTitle
        binding.noDataFound.errorMessage.text = getString(R.string.please_try_again_later)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setActionBarTitle("Teams")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.sorting_menu, menu)
        menu?.findItem(R.id.sortMenuItem)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId) {
            R.id.actionSortAlphabetically -> orderSubject.onNext(TeamListOrderBy(TeamListOrder.Alphabetically()))
            R.id.actionSortByWins -> orderSubject.onNext(TeamListOrderBy(TeamListOrder.HighestWins()))
            R.id.actionSortByLosses -> orderSubject.onNext(TeamListOrderBy(TeamListOrder.MostLost()))
        }
        return true
    }
}