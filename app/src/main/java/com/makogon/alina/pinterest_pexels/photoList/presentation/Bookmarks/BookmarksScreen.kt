package com.makogon.alina.pinterest_pexels.photoList.presentation.Bookmarks

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.makogon.alina.pinterest_pexels.R
import com.makogon.alina.pinterest_pexels.core.presentation.shimmerEffect
import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList.Category
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList.PhotoListEvent
import com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList.PhotoListState
import com.makogon.alina.pinterest_pexels.photoList.presentation.components.BookmarksItem
import com.makogon.alina.pinterest_pexels.photoList.util.CustomLinearProgressBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookmarksScreen(
    photoState: PhotoListState,
    navHostController: NavHostController,
    onEvent: (PhotoListEvent) -> Unit
) {
    val bookmarks = remember { mutableStateListOf<Photo>() }

    LaunchedEffect(Unit) {
        onEvent(PhotoListEvent.Paginate(Category.BOOKMARKED))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 7.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.bookmarks),
                            modifier = Modifier.align(Alignment.TopCenter),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            )
        },
    ) {
        if (!bookmarks.isEmpty() && !photoState.bookmarksList.isEmpty()) {
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
                    items(photoState.bookmarksList.size) { index ->
                        BookmarksItem(
                            photo = photoState.bookmarksList[index],
                            navHostController = navHostController,
                            modifier = Modifier.fillMaxWidth()
                                .aspectRatio(1f)
                                .padding(7.dp)
                                .clip(shape = RoundedCornerShape(4.dp))
                                .shimmerEffect()
                        )
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
                    Text(
                        color = Color.White,
                        text = stringResource(R.string.you_haven_t_saved_anything_yet)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.background(Color.Transparent),
                        onClick = {
                            navHostController.navigate("Home")
                            onEvent(PhotoListEvent.Navigate)
                        }
                    ) {
                        Text(
                            color = Color.Red,
                            text = stringResource(R.string.explore)
                        )
                    }
                }
            }
        }
    }
}