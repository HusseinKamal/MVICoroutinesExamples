package com.hussein.coroutinesexample.mvi_job

data class ViewState(val loading: Boolean, val data: List<Movie>, val error: String?)
