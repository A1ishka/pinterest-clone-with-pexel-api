package com.makogon.alina.pinterest_pexels.core.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.makogon.alina.pinterest_pexels.R
import com.makogon.alina.pinterest_pexels.details.presentation.DetailsScreen
import com.makogon.alina.pinterest_pexels.photoList.presentation.Bookmarks.BookmarksScreen
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList.PhotoListEvent
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList.PhotoListViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(context: Context) {

    val photoListViewModel = hiltViewModel<PhotoListViewModel>()
    //val photoState = photoListViewModel.photoListState.collectAsState().value
    val photoState = photoListViewModel.photoListState.value
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                bottomNavController = bottomNavController,
                onEvent = photoListViewModel::onEvent
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = NavItems.Home.route
            ) {
                composable(NavItems.Home.route) {
                    HomeScreen(
                        photoState = photoState,
                        navHostController = bottomNavController,
                        onEvent = photoListViewModel::onEvent
                    )
                }
                composable(NavItems.Bookmarks.route) {
                    BookmarksScreen(
                        photoState = photoState,
                        navHostController = bottomNavController,
                        onEvent = photoListViewModel::onEvent
                    )
                }
                composable(
                        NavItems.Details.route + "/{photoId}",
                arguments = listOf(navArgument("photoId") { type = NavType.IntType })
                ) {
                    backStackEntry ->  DetailsScreen(backStackEntry, bottomNavController, context)
            }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(bottomNavController: NavHostController, onEvent: (PhotoListEvent) -> Unit) {
    val items = listOf(
        BottomItem(
            selectedIcon = R.drawable.home_button_active,
            unselectedIcon = R.drawable.home_button_inactive,
        ),
        BottomItem(
            selectedIcon = R.drawable.bookmark_active,
            unselectedIcon = R.drawable.bookmark_inactive,
        )
    )

    val selected = remember { mutableStateOf(0) }

    NavigationBar {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(
                    selected = selected.value == index,
                    onClick = {
                        selected.value = index
                        when (selected.value) {
                            0 -> {
                                onEvent(PhotoListEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(NavItems.Home.route)
                            }

                            1 -> {
                                onEvent(PhotoListEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(NavItems.Bookmarks.route)
                            }
                        }
                    },
                    icon = {
                        Image(
                            painter = painterResource(if (selected.value == index) bottomItem.selectedIcon else bottomItem.unselectedIcon),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}

data class BottomItem(
    val selectedIcon: Int,
    val unselectedIcon: Int
)