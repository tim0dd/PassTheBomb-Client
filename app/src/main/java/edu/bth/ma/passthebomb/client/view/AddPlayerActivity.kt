package edu.bth.ma.passthebomb.client.view

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.viewmodel.AddPlayerVm


class AddPlayerActivity : ActionBarActivity() {
    override val vm: AddPlayerVm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_add_player)

        title = "Add Players"

        val gameSettings = intent.getSerializableExtra("GAME_SETTINGS") as GameSettings?
        vm.init(gameSettings)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_add_player)
        val adapter =
            PlayerListAdapter(
                vm
            )
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        val playerNamesObserver = Observer<ArrayList<String>> { adapter.notifyDataSetChanged()}
        vm.playerNames.observe(this, playerNamesObserver)

        val addPlayerButton = findViewById<Button>(R.id.button_add_player)
        addPlayerButton.setOnClickListener {
            val dia = Dialogs(this)
            dia.showStringInputDialog("Player Name") {
                if(it.trim().isNotEmpty()){
                    vm.addPlayer(it)
                }else{
                    Toast.makeText(this, "No empty player names, please!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        val startGameButton = findViewById<Button>(R.id.button_start_game_for_real)
        startGameButton.setOnClickListener{
            if(vm.checkConfiguration()){
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("GAME_SETTINGS", vm.getGameSettingsWithPlayer())
                intent.putParcelableArrayListExtra("challenges", vm.challenges)
                startActivity(intent)
            }
        }
    }

    class PlayerListAdapter(var vm: AddPlayerVm)
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
            return ItemViewHolder(
                adapterLayout
            )
        }

    }
}