package edu.bth.ma.passthebomb.client.view.activities

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.view.adapter.PlayerListAdapter
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.AddPlayerVm


class AddPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_player)

        val vm: AddPlayerVm by viewModels()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_add_player)
        val adapter = PlayerListAdapter(this, vm)
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
}