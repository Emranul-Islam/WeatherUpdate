package com.emranul.weatherupdate.base.ui

import androidx.core.app.ActivityCompat

class ActivityScreenSwitcher : ScreenSwitcher<ActivityScreen> {
    private var mActivity: BaseActivity<*>? = null

    fun attach(mActivity: BaseActivity<*>) {
        this.mActivity = mActivity
    }

    fun detach() {
        this.mActivity = null
    }

    override fun open(mScreen: ActivityScreen) {
        when (mActivity) {
            null -> {

                return
            }
            else -> {
                try {
                    val intent = mScreen.intent(mActivity!!)
                    ActivityCompat.startActivity(
                        mActivity!!, intent, mScreen.activityOptions(mActivity!!)
                    )
                } catch (_: Exception) {

                }

            }
        }
    }

    override fun goBack() {
        if (mActivity != null) {
            mActivity!!.onBackPressed()
        }
    }
}
