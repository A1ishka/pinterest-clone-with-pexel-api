package com.makogon.alina.pinterest_pexels.core.presentation

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.makogon.alina.pinterest_pexels.R
import com.makogon.alina.pinterest_pexels.photoList.presentation.BookmarksScreen
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoListEvent
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoListViewModel

@Composable
fun MainScreen() {

    val photoListViewModel = hiltViewModel<PhotoListViewModel>()
    val photoState = photoListViewModel.photoListState.collectAsState().value
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
                    HomeScreen(navHostController = bottomNavController)
                }
                composable(NavItems.Bookmarks.route) {
                    BookmarksScreen(navHostController = bottomNavController)
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

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(selected = selected.intValue == index,
                    onClick = {
                        selected.intValue = index
                        when (selected.intValue) {
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
                        //логика выбранного
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