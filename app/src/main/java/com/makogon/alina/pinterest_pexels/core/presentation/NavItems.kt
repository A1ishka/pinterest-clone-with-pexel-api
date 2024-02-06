package com.makogon.alina.pinterest_pexels.core.presentation

sealed class NavItems(
    val route: String
) {
    object Home : NavItems(
        route = "home"
    )

    object Bookmarks : NavItems(
        route = "bookmarks"
    )

    object Details : NavItems(
        route = "details"
    )
}