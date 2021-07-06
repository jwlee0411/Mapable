package app.jw.mapable.gm.threadtask

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message


abstract class ThreadTask<T1, T2> : Runnable {
    // Argument
    var mArgument: T1? = null

    // Result
    var mResult: T2? = null

    // Handle the result
    val WORK_DONE = 0

    @SuppressLint("HandlerLeak")
    var mResultHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            // Call onPostExecute
            onPostExecute(mResult)
        }
    }

    // Execute
    fun execute(arg: T1) {
        // Store the argument
        mArgument = arg

        // Call onPreExecute
        onPreExecute()

        // Begin thread work
        val thread = Thread(this)
        thread.start()
    }

    override fun run() {
        // Call doInBackground
        mResult = doInBackground(mArgument)

        // Notify main thread that the work is done
        mResultHandler.sendEmptyMessage(WORK_DONE)
    }

    // onPreExecute
    protected abstract fun onPreExecute()

    // doInBackground
    protected abstract fun doInBackground(arg: T1?): T2

    // onPostExecute
    protected abstract fun onPostExecute(result: T2?)
}