package com.android.fenya.tasks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.content.Intent
import android.util.Log


class MainActivity : AppCompatActivity() {

    companion object {
        const val ARGS_DELAY_TIME: String = "delay_time"

        const val MILLIS_PER_SECOND: Long = 1000
        const val SECONDS_TO_DELAY: Long = 2
    }

    private val LOG_TAG: String = MainActivity::class.java::getName.toString()

    private var mTimer: CountDownTimer? = null
    private var mDelayTime: Long = SECONDS_TO_DELAY * MILLIS_PER_SECOND

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        Log.i(LOG_TAG, "onStart")
        startTimer(mDelayTime)
    }

    private fun startTimer(countDownMillis: Long) {
        mTimer = object : CountDownTimer(countDownMillis, MILLIS_PER_SECOND) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                startActivity(Intent(this@MainActivity, TimerActivity::class.java))
                finish()
            }
        }.start()
    }

    private fun stopTimer() {
        mTimer?.cancel()
    }

    override fun onStop() {
        super.onStop()

        Log.i(LOG_TAG, "onStop")
        stopTimer()
    }

    override fun onPause() {
        super.onPause()

        Log.i(LOG_TAG, "onPause")
        stopTimer()
    }

    override fun onResume() {
        super.onResume()

        Log.i(LOG_TAG, "onResume")
        startTimer(mDelayTime)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Log.i(LOG_TAG, "onRestoreInstanceState")
        mDelayTime = savedInstanceState.getLong(ARGS_DELAY_TIME)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Log.i(LOG_TAG, "onSaveInstanceState")
        outState?.run {
            putLong(ARGS_DELAY_TIME, mDelayTime)
        }

        super.onSaveInstanceState(outState)
    }
}
