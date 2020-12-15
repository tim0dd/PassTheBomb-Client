package edu.bth.ma.passthebomb.client.view

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.viewmodel.DatabaseVm
import edu.bth.ma.passthebomb.client.viewmodel.EventCreator

open class ActionBarActivity : EventProcessingActivity() {

    open val vm by viewModels<DatabaseVm>()

    override fun getEventCreator(): EventCreator {
        return vm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}