package com.udacity


sealed class ButtonState {
    object Clicked : ButtonState()
    object Downloading : ButtonState()
    object Complete : ButtonState()
}