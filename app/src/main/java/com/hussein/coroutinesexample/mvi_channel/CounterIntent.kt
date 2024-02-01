package com.hussein.coroutinesexample.mvi_channel

sealed class CounterIntent {
    object Increment : CounterIntent()
    object Decrement : CounterIntent()
}