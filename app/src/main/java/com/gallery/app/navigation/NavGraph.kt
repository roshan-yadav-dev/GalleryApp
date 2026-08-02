package com.gallery.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gallery.app.feature.albums.AlbumsScreen
import com.gallery.app.feature.favorites.FavoritesScreen
import com.gallery.app.feature.gallery.GalleryScreen
import com.gallery.app.feature.search.SearchScreen
import com.gallery.app.feature.settings.SettingsScreen
import com.gallery.app.feature.trash.TrashScreen
import com.gallery.app.feature.viewer.MediaViewerScreen

@Composable
fun AppNavGraph(
    thumbnailManager: com.gallery.app.core.editor.thumbnail.FrameThumbnailManager,
    navController: NavHostController = rememberNavController()
) {
    val bottomNavItems = listOf(
        Screen.Gallery,
        Screen.Albums,
        Screen.Favorites,
        Screen.Search,
        Screen.Trash,
        Screen.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                item.icon?.let {
                                    Icon(it, contentDescription = item.title)
                                }
                            },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Gallery.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                route = Screen.Gallery.route,
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) }
            ) {
                GalleryScreen(
                    onEditVideo = { uri ->
                        navController.navigate(Screen.VideoEditor.createRoute(uri))
                    },
                    onEditImage = { uri ->
                        navController.navigate(Screen.ImageEditor.createRoute(uri))
                    }
                )
            }

            composable(route = Screen.Albums.route) {
                AlbumsScreen(
                    onAlbumClick = { albumId ->
                        navController.navigate(Screen.AlbumDetail.createRoute(albumId))
                    }
                )
            }

            composable(
                route = Screen.AlbumDetail.route,
                arguments = listOf(navArgument("albumId") { type = NavType.LongType })
            ) {
                com.gallery.app.feature.albums.AlbumDetailScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onMediaClick = { mediaId ->
                        navController.navigate(Screen.Viewer.createRoute(mediaId))
                    }
                )
            }


            composable(route = Screen.Favorites.route) {
                FavoritesScreen(
                    onMediaClick = { mediaId ->
                        navController.navigate(Screen.Viewer.createRoute(mediaId))
                    }
                )
            }

            composable(route = Screen.Search.route) {
                SearchScreen(
                    onMediaClick = { mediaId ->
                        navController.navigate(Screen.Viewer.createRoute(mediaId))
                    }
                )
            }

            composable(route = Screen.Trash.route) {
                TrashScreen()
            }

            composable(route = Screen.Settings.route) {
                SettingsScreen()
            }

            composable(
                route = Screen.Viewer.route,
                arguments = listOf(navArgument("mediaId") { type = NavType.LongType })
            ) {
                MediaViewerScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onEditVideo = { videoUri ->
                        navController.navigate(Screen.VideoEditor.createRoute(videoUri))
                    },
                    onEditImage = { imageUri ->
                        navController.navigate(Screen.ImageEditor.createRoute(imageUri))
                    }
                )
            }

            composable(
                route = Screen.VideoEditor.route,
                arguments = listOf(navArgument("videoUri") { type = NavType.StringType; defaultValue = "" })
            ) { backStackEntry ->
                val videoUriEncoded = backStackEntry.arguments?.getString("videoUri") ?: ""
                val videoUriDecoded = java.net.URLDecoder.decode(videoUriEncoded, "UTF-8")
                com.gallery.app.feature.editor.VideoEditorScreen(
                    videoUriString = videoUriDecoded,
                    onNavigateBack = { navController.popBackStack() },
                    thumbnailManager = thumbnailManager
                )
            }

            composable(
                route = Screen.ImageEditor.route,
                arguments = listOf(navArgument("imageUri") { type = NavType.StringType; defaultValue = "" })
            ) { backStackEntry ->
                val imageUriEncoded = backStackEntry.arguments?.getString("imageUri") ?: ""
                val imageUriDecoded = java.net.URLDecoder.decode(imageUriEncoded, "UTF-8")
                com.gallery.app.feature.editor.image.ImageEditorScreen(
                    imageUriString = imageUriDecoded,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
