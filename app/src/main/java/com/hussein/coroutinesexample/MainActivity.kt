package com.hussein.coroutinesexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hussein.coroutinesexample.mvi_channel.CounterIntent
import com.hussein.coroutinesexample.mvi_channel.CounterViewModel
import com.hussein.coroutinesexample.mvi_job.Intent
import com.hussein.coroutinesexample.mvi_job.MovieViewModel
import com.hussein.coroutinesexample.ui.theme.CoroutinesExampleTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<CounterViewModel>()

    private val viewModelMovie by viewModels<MovieViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoroutinesExampleTheme {
                Surface(color = MaterialTheme.colorScheme.background ,
                    modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        CounterScreen(viewModel = viewModel)
                        Spacer(modifier = Modifier.height(20.dp))
                        MovieScreen(viewModelMovie)
                    }

                }
            }
        }
    }
    @Composable
    fun CounterScreen(viewModel: CounterViewModel) {
        val viewState = viewModel.viewState.collectAsState()

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Count: ${viewState.value.count}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.sendIntent(CounterIntent.Increment) }) {
                Text("Increment")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.sendIntent(CounterIntent.Decrement) }) {
                Text("Decrement")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MovieScreen(viewModel: MovieViewModel) {

        val viewState by viewModel.viewState.collectAsState()

        LaunchedEffect(viewState) {
            if (viewState.error != null || viewState.loading) {
                viewModel.onIntent(Intent.FetchData(1))
            }
        }

        Scaffold(
            snackbarHost = {
                //SnackbarHost(hostState = it)
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                if (viewState.loading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Films group")
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn(modifier = Modifier.fillMaxWidth().border(width = 1.dp, color = Color.Red, shape = RoundedCornerShape(8.dp)).padding(16.dp)) {
                        items(viewState.data) { movie ->
                            Text(text = movie.title)
                            Spacer(modifier = Modifier.height(10.dp))

                        }
                    }
                }
            }
        }
    }
}