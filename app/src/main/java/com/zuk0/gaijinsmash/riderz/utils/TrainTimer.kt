package com.zuk0.gaijinsmash.riderz.utils

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.orhanobut.logger.Logger

class TrainTimer(): LifecycleObserver {

    private var startTimeStamp = 0L
    private var stopTimeStamp = 0L
    private var startingTimeInMillis = 0L
    private var remainingTimeInMillis = 0L
    private var timer: CountDownTimer? = null

    var timeStampOfUpdateLiveData = MutableLiveData<Long>()
    var startingTimeInMinutes: Int = 0

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            CrashLogUtil.log(Logger.INFO, TAG, "create timer")
            startTimeStamp = System.currentTimeMillis()
            timer = getCountDownTimer(timeStampOfUpdateLiveData, startingTimeInMinutes)
        } else {
            //restore state
            CrashLogUtil.log(Logger.INFO, TAG, "restore timer")
            stopTimeStamp = savedInstanceState.getLong(KEY_STOP_TIMESTAMP, TIMER_FINISHED)
            remainingTimeInMillis = savedInstanceState.getLong(KEY_TIME_REMAINING, TIMER_FINISHED)

            //calculate time differences
            val now = System.currentTimeMillis()
            val difference = stopTimeStamp - now
            timer = getCountDownTimer(timeStampOfUpdateLiveData, difference.toInt())
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        CrashLogUtil.log(Logger.INFO, TAG, "start timer")
        timer?.start()
    }

    fun onResume() {

    }

    fun onPaused() {

    }

    /**
     * public method - call this in saveInstanceState() of fragment/activity
     */
    fun saveState(outState: Bundle) {
        stopTimeStamp = System.currentTimeMillis()
        //save state
        val bundle = Bundle()
        bundle.putLong(KEY_TIME_REMAINING, remainingTimeInMillis)

        if(startTimeStamp > 0L && stopTimeStamp > 0L) {
            bundle.putLong(KEY_START_TIMESTAMP, startTimeStamp)
            bundle.putLong(KEY_STOP_TIMESTAMP, stopTimeStamp)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        CrashLogUtil.log(Logger.INFO, TAG, "destroy timer")
        timer?.cancel()
        timer = null
    }

    private fun getCountDownTimer(liveData: MutableLiveData<Long>, startingTimeInMinutes: Int) : CountDownTimer {

        startingTimeInMillis = (startingTimeInMinutes * 60000).toLong()

        return object : CountDownTimer(startingTimeInMillis, 1000) { //1 second interval
            override fun onTick(millisUntilFinished: Long) {
                Log.i(TAG, "onTick: $millisUntilFinished")
                remainingTimeInMillis = millisUntilFinished
                liveData.postValue(System.currentTimeMillis())
            }

            override fun onFinish() {
                Log.i(TAG, "onFinish")
                liveData.postValue(TIMER_FINISHED)
            }
        }
    }

    companion object {
        const val TAG = "TrainTimer"
        const val TIMER_FINISHED = -1L
        const val KEY_TIME_REMAINING = "KEY_TIME_REMAINING"
        const val KEY_START_TIMESTAMP = "KEY_START_TIMESTAMP"
        const val KEY_STOP_TIMESTAMP = "KEY_STOP_TIMESTAMP"
    }
}