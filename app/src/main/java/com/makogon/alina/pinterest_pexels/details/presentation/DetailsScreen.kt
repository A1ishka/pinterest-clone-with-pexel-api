package com.makogon.alina.pinterest_pexels.details.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.makogon.alina.pinterest_pexels.R
import com.makogon.alina.pinterest_pexels.photoList.data.remote.PhotoApi
import com.makogon.alina.pinterest_pexels.photoList.util.downloader.AndroidDownloader

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    context: Context
) {
    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value

    var isSelected by remember { mutableStateOf(false) }

    val ImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(/*PhotoApi.IMAGE_BASE_URL +*/ detailsState.photo?.url)
            //.size(Size.ORIGINAL)
            .setHeader("Authorization", PhotoApi.API_KEY)
            .build()
    ).state

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                    ) {
                        detailsState.photo?.let { photo ->
                            Text(
                                modifier = Modifier//.padding(start = 16.dp)
                                    .align(Alignment.TopCenter),
                                text = photo.photographer,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        bottomBar = {
            if (ImageState is AsyncImagePainter.State.Success) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(200.dp)
                ) {
                    Button(
                        onClick = {
                            val downloader = AndroidDownloader(context)
                            detailsState.photo?.url?.let { downloader.downloadFile(it) }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.download_button),
                            contentDescription = "Download"
                        )
                    }
                    Button(
                        onClick = {

                            //!detailsState.isFavorite
                            //add to bookmarkedList detailsState.photo.id
                            //upsertBookmarkedPhotoList(//BList)
                            isSelected = !isSelected

                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isSelected) R.drawable.bookmark_active else R.drawable.bookmark_inactive
                            ),
                            contentDescription = "Bookmarked"
                        )
                    }
                }
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (ImageState is AsyncImagePainter.State.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(R.string.image_not_found)
                        )
                        Button(
                            onClick = {
                                navController.navigate("Home")
                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = stringResource(R.string.explore)
                            )
                        }
                    }
                }

                if (ImageState is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        painter = ImageState.painter,
                        contentDescription = detailsState.photo?.alt,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

