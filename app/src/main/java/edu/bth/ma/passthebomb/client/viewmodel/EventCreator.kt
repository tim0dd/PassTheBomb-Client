package edu.bth.ma.passthebomb.client.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData

interface EventCreator {
    val events: MutableLiveData<(AppCompatActivity) -> Unit>
}