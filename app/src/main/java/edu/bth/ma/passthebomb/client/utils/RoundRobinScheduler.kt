package edu.bth.ma.passthebomb.client.utils

import android.util.Log

class RoundRobinScheduler(val numElements: Int, val randomShuffleEachRound: Boolean,
                          val onNewRound: (Int) -> Unit) {

    private val schedule: ArrayList<ArrayList<Int>> = ArrayList()
    private var nextActive = 0
    private var nextPosition = 0
    private var round = 0
    var currentValue = -1
    init{
        schedule.add(ArrayList())
        schedule.add(ArrayList())
        scheduleRound(0)
    }

    private fun scheduleRound(slot: Int){
        var shuffledOrder:ArrayList<Int>
        if(randomShuffleEachRound){
            shuffledOrder = (0..numElements-1).shuffled().toCollection(ArrayList<Int>())
        }else{
            shuffledOrder = (0..numElements-1).toCollection(ArrayList<Int>())
        }
        schedule[slot] = shuffledOrder
    }

    fun peekNextElement(): Int{
        return schedule[nextActive][nextPosition]
    }

    fun nextElement(): Int{
        Log.i("SCHEDULER", "next")
        currentValue = peekNextElement()
        if(nextPosition==numElements-1){
            nextActive = (nextActive+1)%2
            do{
                scheduleRound(nextActive)
            }while(schedule[(nextActive+1)%2][numElements-1]==schedule[nextActive][0])
        }else if(nextPosition == 0){
            onNewRound(round)
            round++
        }
        nextPosition = (nextPosition + 1) % numElements
        return currentValue
    }
}