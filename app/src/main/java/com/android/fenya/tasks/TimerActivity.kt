package com.android.fenya.tasks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView


class TimerActivity : AppCompatActivity() {

    companion object {
        const val ARGS_COUNTING_TIME: String = "countintg_time"
        const val ARGS_TIMER_TIME: String = "timer_time"
        const val ARGS_TIMER_IS_RUNNING: String = "timer_is_running"

        const val MILLIS_PER_SECOND: Long = 1000
        const val SECONDS_TO_COUNTDOWN: Long = 20

        const val TEN = 10
        const val TWENTY = 20
        const val HUNDRED = 100
        const val THOUSAND = 1000
    }

    private val LOG_TAG: String = TimerActivity::class.java::getName.toString()

    lateinit var mButton: Button
    lateinit var mTextView: TextView

    private var mTimer: CountDownTimer? = null
    private var mCountingTime: Long = SECONDS_TO_COUNTDOWN * MILLIS_PER_SECOND
    private var mTimerIsRunning: Boolean = false

    private lateinit var mUnits: Array<String>
    private lateinit var mTens: Array<String>
    private lateinit var mHundreds: Array<String>

    private fun initResources() {
        mUnits = resources.getStringArray(R.array.units)
        mTens = resources.getStringArray(R.array.tens)
        mHundreds = resources.getStringArray(R.array.hundreds)
    }

    private fun initValues() {
        mCountingTime = SECONDS_TO_COUNTDOWN * MILLIS_PER_SECOND
        mTimerIsRunning = false
        mButton.setText(R.string.start)
        mTextView.setText(R.string.nothing)
    }

    private fun startTimer(countDownMillis: Long) {
        if (mTimerIsRunning) {
            mButton.setText(R.string.stop)
            mTimer = object : CountDownTimer(countDownMillis, MILLIS_PER_SECOND) {
                override fun onTick(millisUntilFinished: Long) {
                    mCountingTime = millisUntilFinished
                    mTextView.text = num2str((SECONDS_TO_COUNTDOWN - mCountingTime / MILLIS_PER_SECOND).toInt())
                }

                override fun onFinish() {
                    initValues()
                }
            }.start()
        }
    }

    private fun stopTimer() {
        mButton.setText(R.string.start)
        mTimer?.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(LOG_TAG, "onCreate")
        setContentView(R.layout.activity_timer)

        mButton = findViewById(R.id.timerButton)
        mTextView = findViewById(R.id.timerTextView)

        initValues()
        initResources()

        mButton.setOnClickListener {
            if (!mTimerIsRunning) {
                mTimerIsRunning = true
                startTimer(mCountingTime)
            } else {
                mTimerIsRunning = false
                stopTimer()
                initValues()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Log.i(LOG_TAG, "onResume")
        startTimer(mCountingTime)
    }

    override fun onPause() {
        super.onPause()

        Log.i(LOG_TAG, "onPause")
        stopTimer()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Log.i(LOG_TAG, "onRestoreInstanceState")
        mCountingTime = savedInstanceState.getLong(ARGS_COUNTING_TIME)
        mTextView.text = savedInstanceState.getString(ARGS_TIMER_TIME)
        mTimerIsRunning = savedInstanceState.getBoolean(ARGS_TIMER_IS_RUNNING)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Log.i(LOG_TAG, "onSaveInstanceState")
        outState?.run {
            putLong(ARGS_COUNTING_TIME, mCountingTime)
            putString(ARGS_TIMER_TIME, mTextView.toString())
            putBoolean(ARGS_TIMER_IS_RUNNING, mTimerIsRunning)
        }

        super.onSaveInstanceState(outState)
    }

    private fun num2str(number: Int): String {
        if (number < TWENTY) {
            return mUnits[number]
        }

        if (number < HUNDRED) {
            return mTens[number / TEN] + (if (number % TEN != 0) " " else "") + mUnits[number % TEN]
        }

        return if (number < THOUSAND) {
            mHundreds[number / HUNDRED] + (if (number % HUNDRED != 0) " " else "") + num2str(number % HUNDRED)
        } else THOUSAND.toString()

    }
}
