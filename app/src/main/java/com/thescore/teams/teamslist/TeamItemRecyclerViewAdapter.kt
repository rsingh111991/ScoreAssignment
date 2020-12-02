package com.thescore.teams.teamslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thescore.R
import com.thescore.databinding.TeamItemLayoutBinding
import com.thescore.teams.uimodel.ActionableItem

class TeamItemRecyclerViewAdapter: RecyclerView.Adapter<TeamItemRecyclerViewAdapter.ViewHolder>() {
    private var teamArrayList : MutableList<ActionableItem> = mutableListOf()

    fun updateTeamData(teamList : List<ActionableItem>){
        teamArrayList.clear()
        teamArrayList.addAll(teamList)
        notifyDataSetChanged()
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(private val itemLayoutBinding:  TeamItemLayoutBinding) : RecyclerView.ViewHolder(itemLayoutBinding.root) {
        fun bind(actionableItem: ActionableItem){
            val team = actionableItem.team
            itemLayoutBinding.cardView.setOnClickListener {
                actionableItem.clickAction.invoke()
            }
            itemLayoutBinding.teamName.text = team.teamFullName
            val context = itemLayoutBinding.root.context
            itemLayoutBinding.numberOfLosses.text = context.getString(R.string.no_of_Losses, team.losses)
            itemLayoutBinding.numberOfWins.text = context.getString(R.string.no_of_wins,team.wins)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        return ViewHolder(TeamItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context),
             viewGroup, false))

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(teamArrayList.get(position))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = teamArrayList.size

}