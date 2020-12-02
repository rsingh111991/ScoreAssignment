package com.thescore.teams.teamdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.thescore.databinding.DetailItemViewLayoutBinding
import com.thescore.databinding.HeaderItemBinding
import com.thescore.teams.teamdetail.sort.RecyclerViewItemType
import com.thescore.teams.teamdetail.sort.UiPlayer

class PlayerRoosterAdapter(private val uiPlayers: List<UiPlayer>) :
    RecyclerView.Adapter<PlayerRoosterAdapter.BaseViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_PLAYER = 1
    }

    abstract class BaseViewHolder(itemView: ViewBinding) : RecyclerView.ViewHolder(itemView.root) {
        abstract fun bind(uiPlayer: UiPlayer)
    }

    class HeaderViewHolder(private val headerItemBinding: HeaderItemBinding) :
        BaseViewHolder(headerItemBinding) {
        override fun bind(uiPlayer: UiPlayer) {
            headerItemBinding.position.text = uiPlayer.currentPosition
        }
    }

    class PlayersViewHolder(private val detailItemViewLayoutBinding: DetailItemViewLayoutBinding) :
        BaseViewHolder(detailItemViewLayoutBinding) {
        override fun bind(uiPlayer: UiPlayer) {
            val player = uiPlayer.playersElements

            detailItemViewLayoutBinding.mark.text = uiPlayer.initials
            (player?.firstName + " " + player?.lastName).also {
                detailItemViewLayoutBinding.playerName.text = it
            }
            detailItemViewLayoutBinding.playerPosition.text = player?.position
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = HeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                HeaderViewHolder(view)
            }
            else -> {
                val view = DetailItemViewLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                PlayersViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val player = uiPlayers[position]
        holder.bind(player)
    }

    override fun getItemCount() = uiPlayers.size

    override fun getItemViewType(position: Int): Int {
        return when (uiPlayers[position].recyclerViewItemType.itemType) {
            RecyclerViewItemType.Header().itemType -> TYPE_HEADER
            RecyclerViewItemType.Player().itemType -> TYPE_PLAYER
            else -> TYPE_PLAYER
        }
    }
}