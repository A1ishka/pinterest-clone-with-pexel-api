package com.makogon.alina.pinterest_pexels.core.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.makogon.alina.pinterest_pexels.R
import com.makogon.alina.pinterest_pexels.photoList.data.remote.PhotoApi.Companion.PER_PAGE
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList.Category
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList.PhotoListEvent
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList.PhotoListState
import com.makogon.alina.pinterest_pexels.photoList.presentation.components.CollectionItem
import com.makogon.alina.pinterest_pexels.photoList.presentation.components.FeaturedCollection
import com.makogon.alina.pinterest_pexels.photoList.util.CustomLinearProgressBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    photoState: PhotoListState,
    navHostController: NavHostController,
    onEvent: (PhotoListEvent) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var selectedTitle by remember { mutableStateOf("") }
    var collections by remember { mutableStateOf<List<FeaturedCollection>?>(null) }
    var lastUpdateTime: Long = System.currentTimeMillis()

    val context = LocalContext.current
    val hasNetworkConnection = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)
        }
        hasNetworkConnection.value =
            networkCapabilities != null && networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
//                query = photoState.searchText,
//                onQueryChange = { PhotoListEvent.ChangeSearchText(it) },
                query = searchText,
                onQueryChange = {
                    searchText = it
                    val currentTime = System.currentTimeMillis()
                    val elapsedTime = currentTime - lastUpdateTime
                    if (elapsedTime >= 3000) {
                        lastUpdateTime = currentTime

                        PhotoListEvent.ChangeSearchText(searchText)
                        PhotoListEvent.PerformPhotoSearch(searchText)
                    }
                },
                onSearch = { active = false },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(text = "Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon"
                    )
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            if (searchText.isNotEmpty() || selectedTitle.isNotEmpty()) {
                                searchText = ""
                                selectedTitle = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close search icon"
                    )
                },
            ) {

            }
        }
    ) {
        if (hasNetworkConnection.value) {
            if (photoState.isLoading == true) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CustomLinearProgressBar()
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 85.dp),
                        contentPadding = PaddingValues(4.dp, 8.dp)
                    ) {
                        items(PER_PAGE) { index ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(7.dp)
                                    .clip(shape = RoundedCornerShape(4.dp))
                                    .shimmerEffect()
                            )
                        }
                    }
                }
            } else {
                if (photoState.photoList.size < 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                color = Color.White,
                                text = stringResource(R.string.no_results_found)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                modifier = Modifier.background(Color.Transparent),
                                onClick = {
                                    navHostController.navigate("Home")
                                    if (searchText.isNotEmpty() || selectedTitle.isNotEmpty()) {
                                        searchText = ""
                                        selectedTitle = ""
                                    }
                                }
                            ) {
                                Text(
                                    color = Color.Red,
                                    text = stringResource(R.string.explore)
                                )
                            }
                        }
                    }
                } else {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(3),
                        verticalItemSpacing = 7.dp,
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        content = {
                            items(photoState.photoList.size) { photo ->
                                Card(modifier = Modifier.clickable {
                                    navHostController.navigate(
                                        NavItems.Details.route + "/${photoState.photoList[photo].id}"
                                    )
                                }) {
                                    AsyncImage(
                                        model = photo,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .shimmerEffect(),
                                    )
                                    if (photo >= photoState.photoList.size - 1 && !photoState.isLoading) {
                                        onEvent(PhotoListEvent.PerformPhotoSearch(searchText))
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                onEvent(PhotoListEvent.Paginate(Category.PHOTO))
                Row {
                    collections?.let {
                        CollectionList(
                            collections = photoState.featuredCollections,
                            selectedTitle = selectedTitle
                        ) { title ->
                            selectedTitle = title
                            searchText = title
                            PhotoListEvent.ChangeSearchText(title)
                            PhotoListEvent.ChangeSelectedTitle(title)
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        imageVector = Icons.Rounded.WifiOff,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.background(Color.Transparent),
                        onClick = {
                            navHostController.navigate("Home")
                        }
                    ) {
                        Text(
                            color = Color.Red,
                            text = stringResource(R.string.try_again)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CollectionList(
    collections: List<FeaturedCollection>,
    selectedTitle: String,
    onTitleClick: (String) -> Unit
) {

//    val modifiedCollections = mutableListOf<FeaturedCollection>()
//    val selectedCollection = collections.find { it.title == selectedTitle }
//
//    if (selectedCollection != null) {
//        modifiedCollections.add(selectedCollection) //make selected the first one
//        modifiedCollections.addAll(collections.filter { it.title != selectedTitle })
//    } else {
//        modifiedCollections.addAll(collections)
//    }

    LazyColumn {
//    items(modifiedCollections.size) { index ->
//        val collection = modifiedCollections[index]
      items(collections.size) { index ->
         val collection = collections[index]
         CollectionItem(
             collection = collection,
             isSelected = collection.title == selectedTitle,
             onTitleClick = { onTitleClick(collection.title) }
            )
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val photoState = remember { PhotoListState(isLoading = false) }
    val navHostController = rememberNavController()

    HomeScreen(photoState, navHostController, onEvent = {})
}