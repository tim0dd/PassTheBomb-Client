package edu.bth.ma.passthebomb.client.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.GameSettings

class GameOverActivity : ActionBarActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_game_over)

        title = "Results"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val gameSettings: GameSettings = intent.getSerializableExtra("GAME_SETTINGS") as GameSettings?
                ?: GameSettings()
        val scores: ArrayList<Int> = intent.getIntegerArrayListExtra("SCORES") as ArrayList<Int>? ?: ArrayList()

        recyclerView = findViewById(R.id.recycler_view_scores)
        recyclerView.adapter = ScoresAdapter(this, gameSettings.playerList, scores)
        recyclerView.setHasFixedSize(true)

        val buttonRestartGame : Button = findViewById(R.id.button_play_another_round)
        buttonRestartGame.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("GAME_SETTINGS", gameSettings)
            val list = arrayListOf<Parcelable>()
            val challengesMaybeNull: ArrayList<Challenge>? =
                this.intent.getParcelableArrayListExtra("challenges")
            list.addAll(challengesMaybeNull!!)
            intent.putParcelableArrayListExtra("challenges", list)
            startActivity(intent)
        }
        val buttonBackToMenu : Button = findViewById(R.id.button_scores_back_to_menu)
        buttonBackToMenu.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    inner class ScoresAdapter(private val context: Context,
                                       private val playerNames: List<String>,
                                       private val playerScores: List<Int>
    ) : RecyclerView.Adapter<ScoresAdapter.ItemViewHolder>(){
        inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val textViewPlayerName: TextView = view.findViewById(R.id.text_view_score_player_name)
            val textViewScore: TextView = view.findViewById(R.id.text_view_score)
        }

        override fun getItemCount(): Int {
            return playerNames.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.textViewPlayerName.text =  playerNames[position]
            holder.textViewScore.text = playerScores[position].toString()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoresAdapter.ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_score, parent, false)
            return ItemViewHolder(adapterLayout)
        }
    }



}