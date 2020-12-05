package edu.bth.ma.passthebomb.client.view

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.viewmodel.AddPlayerVm


class AddPlayerActivity : AppCompatActivity() {
    val vm: AddPlayerVm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_add_player)

        val gameSettings = intent.getSerializableExtra("GAME_SETTINGS") as GameSettings?
        vm.init(gameSettings)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_add_player)
        val adapter =
            PlayerListAdapter(
                this,
                vm
            )
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        val playerNamesObserver = Observer<ArrayList<String>> { adapter.notifyDataSetChanged()}
        vm.playerNames.observe(this, playerNamesObserver)

        val addPlayerButton = findViewById<Button>(R.id.button_add_player)
        addPlayerButton.setOnClickListener {
            showInputDialog {
                vm.addPlayer(it)
            }
        }

        val startGameButton = findViewById<Button>(R.id.button_start_game_for_real)
        startGameButton.setOnClickListener{
            vm.startGame(this)
        }
    }

    fun showInputDialog(block: (String) -> Unit){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Title")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK", { dialog, which -> block(input.text.toString()) })
        builder.show()
    }

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
            return ItemViewHolder(
                adapterLayout
            )
        }

    }
}