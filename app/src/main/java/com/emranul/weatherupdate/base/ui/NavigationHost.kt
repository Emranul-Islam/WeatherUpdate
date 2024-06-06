package com.emranul.weatherupdate.base.ui

import androidx.appcompat.widget.Toolbar
import com.emranul.weatherupdate.base.ui.ActivityScreenSwitcher

interface NavigationHost {
    /** Called by MainNavigationFragment to setup it's toolbar with the navigation controller. */
    fun registerToolbarWithNavigation(toolbar: Toolbar)

    fun activityScreenSwitcher(): ActivityScreenSwitcher
}
