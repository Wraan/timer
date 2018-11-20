package com.example.wran.minutnik


enum class TimerStatus {
    RUNNING, STOPPED, PAUSED
}

class TimerNumbers(var tensMinutes:Int, var minutes:Int, var tensSeconds:Int, var seconds:Int){
    var status:TimerStatus = TimerStatus.STOPPED

    fun increaseSeconds(){
        if(seconds == 9) {
            seconds = 0
            increaseTensSeconds()
        }
        else
            seconds++
    }
    fun increaseTensSeconds(){
        if(tensSeconds == 5) {
            tensSeconds = 0
            increaseMinutes()
        }
        else
            tensSeconds++
    }
    fun increaseMinutes(){
        if(minutes == 9) {
            minutes = 0
            increaseTensMinutes()
        }
        else
            minutes++
    }
    fun increaseTensMinutes(){
        if(tensMinutes == 5)
            tensMinutes = 0
        else
            tensMinutes++
    }
    fun decreaseTensMinutes(){
        if(tensMinutes == 0)
            tensMinutes = 5
        else
            tensMinutes--
    }
    fun decreaseMinutes(){
        if(minutes == 0) {
            minutes = 9
            decreaseTensMinutes()
        }
        else
            minutes--
    }
    fun decreaseTensSeconds(){
        if(tensSeconds == 0) {
            tensSeconds = 5
            decreaseMinutes()
        }
        else
            tensSeconds--
    }
    fun decreaseSeconds(){
        if(seconds == 0) {
            seconds = 9
            decreaseTensSeconds()
        }
        else
            seconds--
    }
    fun isEnded():Boolean{
        if(tensMinutes==0 && minutes == 0 && tensSeconds == 0 && seconds == 0)
            return true
        return false
    }
    fun getTimeInMilis():Long {
        return ((seconds + tensSeconds*10 + minutes*60 + tensMinutes*600)*1000).toLong()
    }
    fun resetNumbers(){
        tensMinutes = 0
        minutes = 0
        tensSeconds = 0
        seconds = 0
    }
}