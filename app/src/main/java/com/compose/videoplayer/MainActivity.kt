package com.compose.videoplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.ui.PlayerView
import com.compose.videoplayer.ui.theme.VideoPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoPlayerTheme {
                val viewModel = hiltViewModel<MainViewModel>()
                val videoItems by viewModel.videoItems.collectAsState()
                val selectVideoLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = {uri ->
                        uri?.let(viewModel::addVideoUri)
                    }
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    AndroidView(
                        factory = { context ->
                            PlayerView(context).also {
                                it.player = viewModel.player
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IconButton(onClick = {
                        selectVideoLauncher.launch("video/*")
                    }) {
                        Icon(
                            imageVector = Icons.Default.FileOpen, 
                            contentDescription = "Select video"
                        )

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(videoItems) {item ->
                            Text(
                                text = item.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.playVideo(item.contentUri)

                                    }
                                    .padding(16.dp)
                            )
                        }
                    }

                }

            }
        }
    }
}

