package com.emranul.weatherupdate.base.ui

interface ScreenSwitcher<S : Screen> {
    fun open(mScreen: S)
    fun goBack()
}
