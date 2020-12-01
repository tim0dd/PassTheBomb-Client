package edu.bth.ma.passthebomb.client.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.AddPlayerVm

class PlayerListAdapter(private val context: Context, var vm: AddPlayerVm)
    : RecyclerView.Adapter<PlayerListAdapter.ItemViewHolder>(){
    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textViewPlayerName: TextView = view.findViewById(R.id.text_view_player_name)
    }

    override fun getItemCount(): Int {
        return vm.playerNames.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val text = vm.playerNames.value?.get(position)
        holder.textViewPlayerName.text = text
        val deleteButton = holder.view.findViewById<Button>(R.id.button_delete_player)
        deleteButton.setOnClickListener{
            vm.deletePlayer(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_player, parent, false)
        return ItemViewHolder(adapterLayout)
    }

}