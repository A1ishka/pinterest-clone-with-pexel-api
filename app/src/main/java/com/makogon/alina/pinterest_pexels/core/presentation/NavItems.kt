package com.makogon.alina.pinterest_pexels.core.presentation

import com.makogon.alina.pinterest_pexels.R

sealed class NavItems(
    val route: String
) {
    object Home : NavItems(
        route = "home"
    )

    object Bookmarks : NavItems(
        route = "bookmarks"
    )
    object Details: NavItems(
        route="details"
    )
}