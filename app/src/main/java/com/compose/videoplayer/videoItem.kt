package com.compose.videoplayer

import android.net.Uri
import androidx.media3.common.MediaItem

data class videoItem(
    val contentUri: Uri,
    val mediaItem: MediaItem,
    val name: String
)
