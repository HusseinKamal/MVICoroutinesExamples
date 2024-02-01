package com.hussein.coroutinesexample.mvi_channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CounterViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(CounterViewState(0))
    val viewState: StateFlow<CounterViewState> = _viewState.asStateFlow()

    private val intentChannel = Channel<CounterIntent>()
    val intent = intentChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            intent.collect { intent ->
                when (intent) {
                    is CounterIntent.Increment -> _viewState.update { it.copy(count = it.count + 1) }
                    is CounterIntent.Decrement -> _viewState.update { it.copy(count = it.count - 1) }
                }
            }
        }
    }

    fun sendIntent(intent: CounterIntent) {
        viewModelScope.launch { intentChannel.send(intent) }
    }
}