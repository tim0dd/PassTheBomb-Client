package edu.bth.ma.passthebomb.client.utils

class RoundRobinScheduler(
    private val numElements: Int, private val randomShuffleEachRound: Boolean,
    val onNewRound: (Int) -> Unit) {

    private val schedule: ArrayList<ArrayList<Int>> = ArrayList()
    private var nextActive = 0
    private var nextPosition = 0
    var round = 0
    var currentElement = -1
    init{
        schedule.add(ArrayList())
        schedule.add(ArrayList())
        scheduleRound(0)
        nextElement()
    }

    private fun scheduleRound(slot: Int){
        var shuffledOrder:ArrayList<Int>
        shuffledOrder = if(randomShuffleEachRound){
            (0 until numElements).shuffled().toCollection(ArrayList<Int>())
        }else{
            (0 until numElements).toCollection(ArrayList<Int>())
        }
        schedule[slot] = shuffledOrder
    }

    fun peekNextElement(): Int{
        return schedule[nextActive][nextPosition]
    }

    fun nextElement(): Int{
        currentElement = peekNextElement()
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
        return currentElement
    }
}