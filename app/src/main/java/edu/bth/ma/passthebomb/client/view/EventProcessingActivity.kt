package edu.bth.ma.passthebomb.client.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.viewmodel.EventCreator

abstract class EventProcessingActivity: AppCompatActivity() {

    abstract fun getEventCreator(): EventCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getEventCreator().events.observe(this, Observer {
            it(this@EventProcessingActivity)
        })
    }
}