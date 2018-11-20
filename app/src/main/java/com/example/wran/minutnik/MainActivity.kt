package com.example.wran.minutnik

import android.content.Context
import android.media.MediaPlayer
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.wran.minutnik.R.id.minutesText
import com.example.wran.minutnik.R.id.tensMinutesText
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.scheduleAtFixedRate

class MainActivity : AppCompatActivity() {

    private var timerNumbers = TimerNumbers(0,0,0,0)
    private var timer:CountDownTimer? = null

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.i("TAG", "onSaveInstanceState")

        outState?.putCharSequence("seconds", secondsText.text)
        outState?.putCharSequence("tensSeconds", tensSecondsText.text)
        outState?.putCharSequence("minutes", minutesText.text)
        outState?.putCharSequence("tensMinutes", tensMinutesText.text)

        if(timerNumbers.status == TimerStatus.RUNNING)
            outState?.putBoolean("running", true)
        else outState?.putBoolean("running", false)

        pauseTimer()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("TAG", "onRestoreInstanceState")

        timerNumbers = TimerNumbers(Integer.parseInt(savedInstanceState?.getCharSequence("tensMinutes").toString()),
            Integer.parseInt(savedInstanceState?.getCharSequence("minutes").toString()),
            Integer.parseInt(savedInstanceState?.getCharSequence("tensSeconds").toString()),
            Integer.parseInt(savedInstanceState?.getCharSequence("seconds").toString()))
        updateTimerTexts()

        if(savedInstanceState?.getBoolean("running") == true)
            startTimer()
    }

    private fun updateTimerTexts() {
        tensMinutesText.text = timerNumbers.tensMinutes.toString()
        minutesText.text = timerNumbers.minutes.toString()
        tensSecondsText.text = timerNumbers.tensSeconds.toString()
        secondsText.text = timerNumbers.seconds.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addListeners()
    }

    private fun addListeners(){
        tensMinutesPlusButton.setOnClickListener{
            if (timerNumbers.status != TimerStatus.RUNNING) {
                timerNumbers.increaseTensMinutes()
                updateTimerTexts()
            }
        }
        tensMinutesMinusButton.setOnClickListener{
            if (timerNumbers.status != TimerStatus.RUNNING) {
                timerNumbers.decreaseTensMinutes()
                updateTimerTexts()
            }
        }
        minutesPlusButton.setOnClickListener{
            if (timerNumbers.status != TimerStatus.RUNNING) {
                timerNumbers.increaseMinutes()
                updateTimerTexts()
            }
        }
        minutesMinusButton.setOnClickListener {
            if (timerNumbers.status != TimerStatus.RUNNING) {
                timerNumbers.decreaseMinutes()
                updateTimerTexts()
            }
        }
        tensSecondsPlusButton.setOnClickListener {
            if (timerNumbers.status != TimerStatus.RUNNING) {
                timerNumbers.increaseTensSeconds()
                updateTimerTexts()
            }
        }
        tensSecondsMinusButton.setOnClickListener {
            if (timerNumbers.status != TimerStatus.RUNNING) {
                timerNumbers.decreaseTensSeconds()
                updateTimerTexts()
            }
        }
        secondsPlusButton.setOnClickListener {
            if (timerNumbers.status != TimerStatus.RUNNING) {
                timerNumbers.increaseSeconds()
                updateTimerTexts()
            }
        }
        secondsMinusButton.setOnClickListener {
            if (timerNumbers.status != TimerStatus.RUNNING) {
                timerNumbers.decreaseSeconds()
                updateTimerTexts()
            }
        }
        startButton.setOnClickListener{
            if (timerNumbers.status != TimerStatus.RUNNING) {
                startTimer()
            }
        }
        pauseButton.setOnClickListener {
            pauseTimer()
        }
        stopButton.setOnClickListener {
            stopTimer()
        }
    }
    private fun startTimer(){
        if(timerNumbers.isEnded())
            return
        timerNumbers.status = TimerStatus.RUNNING
        Log.d("STATE", "START")
        timer = timer(timerNumbers.getTimeInMilis(), 998).start()
    }
    private fun pauseTimer(){
        Log.d("STATE", "PAUSE")
        timerNumbers.status = TimerStatus.PAUSED
        timer?.cancel()
    }
    private fun stopTimer(){
        Log.d("STATE", "STOP")
        timerNumbers.status = TimerStatus.STOPPED
        timerNumbers.resetNumbers()
        updateTimerTexts()
        timer?.cancel()

    }

    private fun timer(millisInFuture:Long,countDownInterval:Long):CountDownTimer{
        return object: CountDownTimer(millisInFuture, countDownInterval){
            override fun onTick(millisUntilFinished: Long){
                timerNumbers.decreaseSeconds()
                updateTimerTexts()
            }
            override fun onFinish() {
                stopTimer()
                vibrate()
                playSound()
            }
        }
    }

    private fun vibrate(){
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
    private fun playSound(){
        val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.ring)
        mediaPlayer.start()
    }

}
