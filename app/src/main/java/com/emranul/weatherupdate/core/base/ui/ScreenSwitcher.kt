package com.emranul.weatherupdate.core.base.ui

interface ScreenSwitcher<S : Screen> {
    fun open(mScreen: S)
    fun goBack()
}
