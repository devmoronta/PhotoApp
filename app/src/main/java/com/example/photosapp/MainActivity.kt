package com.example.photosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.photosapp.ui.NavRoutes
import com.example.photosapp.ui.screens.PhotoDetailScreen
import com.example.photosapp.ui.screens.PhotoListScreen
import com.example.photosapp.ui.theme.PhotosAppTheme
import com.example.photosapp.viewmodel.PhotoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotosAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.PHOTO_LIST
                ) {
                    composable(NavRoutes.PHOTO_LIST) { backStackEntry ->
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry(NavRoutes.PHOTO_LIST)
                        }
                        val viewModel: PhotoListViewModel = hiltViewModel(parentEntry)
                        PhotoListScreen(navController = navController, viewModel = viewModel)
                    }
                    composable(NavRoutes.PHOTO_DETAIL) { backStackEntry ->
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry(NavRoutes.PHOTO_LIST)
                        }
                        val viewModel: PhotoListViewModel = hiltViewModel(parentEntry)
                        PhotoDetailScreen(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}