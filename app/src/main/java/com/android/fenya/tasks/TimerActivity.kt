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
        const val ARGS_IS_RUNNING: String = "is_running"

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
    private var mCountingTime: Long = 0
    private var mIsRunning: Boolean = false

    lateinit var mUnits: Array<String>
    lateinit var mTens: Array<String>
    lateinit var mHundreds: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        mButton = findViewById(R.id.timerButton)
        mTextView = findViewById(R.id.timerTextView)

        initResources()
        initValues()
    }

    private fun initResources() {
        mUnits = resources.getStringArray(R.array.units)
        mTens = resources.getStringArray(R.array.tens)
        mHundreds = resources.getStringArray(R.array.hundreds)
    }

    private fun initValues() {
        mCountingTime = SECONDS_TO_COUNTDOWN * MILLIS_PER_SECOND
        mIsRunning = false
        mButton.setText(R.string.start)
    }

    override fun onStart() {
        super.onStart()

        mButton.setOnClickListener {
            if (!mIsRunning) {
                startTimer(mCountingTime)
            } else {
                stopTimer()
                initValues()
            }
        }
    }

    private fun startTimer(countDownMillis: Long) {
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

        mIsRunning = true
    }

    private fun stopTimer() {
        mTimer?.cancel()
    }

    override fun onStop() {
        super.onStop()

        stopTimer()
    }

    override fun onPause() {
        super.onPause()

        stopTimer()
    }

    override fun onResume() {
        super.onResume()

        if (mIsRunning) startTimer(mCountingTime)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Log.i(LOG_TAG, "onRestoreInstanceState")
        mCountingTime = savedInstanceState.getLong(ARGS_COUNTING_TIME)
        mTextView.text = savedInstanceState.getString(ARGS_TIMER_TIME)
        mIsRunning = savedInstanceState.getBoolean(ARGS_IS_RUNNING)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Log.i(LOG_TAG, "onSaveInstanceState")
        outState?.run {
            putLong(ARGS_COUNTING_TIME, mCountingTime)
            putString(ARGS_TIMER_TIME, mTextView.toString())
            putBoolean(ARGS_IS_RUNNING, mIsRunning)
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
