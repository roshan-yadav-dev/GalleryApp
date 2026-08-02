package com.gallery.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    object Gallery : Screen("gallery", "Gallery", Icons.Default.PhotoLibrary)
    object Albums : Screen("albums", "Albums", Icons.Default.Collections)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Trash : Screen("trash", "Trash", Icons.Default.Delete)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    object Viewer : Screen("viewer/{mediaId}") {
        fun createRoute(mediaId: Long) = "viewer/$mediaId"
    }

    object AlbumDetail : Screen("album_detail/{albumId}") {
        fun createRoute(albumId: Long) = "album_detail/$albumId"
    }

    object VideoEditor : Screen("video_editor?videoUri={videoUri}") {
        fun createRoute(videoUri: String) = "video_editor?videoUri=${java.net.URLEncoder.encode(videoUri, "UTF-8")}"
    }

    object ImageEditor : Screen("image_editor?imageUri={imageUri}") {
        fun createRoute(imageUri: String) = "image_editor?imageUri=${java.net.URLEncoder.encode(imageUri, "UTF-8")}"
    }
}
