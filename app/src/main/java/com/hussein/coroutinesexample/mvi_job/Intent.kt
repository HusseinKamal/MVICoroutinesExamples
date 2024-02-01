package com.hussein.coroutinesexample.mvi_job

sealed class Intent {
    data class FetchData(val page: Int) : Intent()
}