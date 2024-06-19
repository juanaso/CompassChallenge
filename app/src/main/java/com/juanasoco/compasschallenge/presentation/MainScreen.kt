package com.juanasoco.compasschallenge.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    viewModel: CompassChallengeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val charListState by viewModel.every10thChar.collectAsState()
    val wordCountState by viewModel.wordCount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom))
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Every10thCharacter", modifier = Modifier.padding(16.dp))
                LazyColumn {
                    items(charListState) { item ->
                        Text(
                            text = item.toString(), modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .padding(8.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("WordCounter", modifier = Modifier.padding(16.dp))
                LazyColumn {
                    items(wordCountState.entries.toList()) { (word, count) ->
                        Text(text = "$word: $count")
                    }
                }
            }
        }
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = if (viewModel.fetchingState.value) Color.Gray else Color(0xFF6200EE)
            ),
            onClick = {
                if (!viewModel.fetchingState.value) {
                    viewModel.fetchData()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            if (viewModel.fetchingState.value) {
                Text("Fetching")
            } else {
                Text("Fetch Content")
            }
        }
    }
}