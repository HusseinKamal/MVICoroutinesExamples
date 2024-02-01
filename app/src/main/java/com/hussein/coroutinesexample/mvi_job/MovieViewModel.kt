package com.hussein.coroutinesexample.mvi_job

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState(true, emptyList(), null))
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val movieChannel = Channel<List<Movie>>()

    val listMovies = listOf(
        Movie(title = "SuperMan" , image = "http//www.google.com", author = "Author1"),
        Movie(title = "Avatar" , image = "http//www.google.com", author = "Author2"),
        Movie(title = "SpiderMan" , image = "http//www.google.com", author = "Author3"))


    init {
        scope.launch {
            for (movies in movieChannel) {
                _viewState.update { it.copy(data = movies, loading = false) }
            }
        }
    }

    fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.FetchData -> {
                scope.launch {
                    try {
                        // val movies = fetchMovies(intent.page) // replace with actual API call
                        val movies = listMovies
                        movieChannel.send(movies)
                    } catch (e: Exception) {
                        _viewState.update { it.copy(error = e.message) }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}