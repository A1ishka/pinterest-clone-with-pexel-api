package com.makogon.alina.pinterest_pexels.details.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(backStackEntry: NavBackStackEntry, navController: NavController) {

    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value

    var isSelected by remember { mutableStateOf(false) }

//    try {
//        val details = detailsRepository.getDetailsById(id)
//        isSelected = details.isSelected
//    } catch (e: Exception) {
//        // Обработка исключения, если запись не найдена
//    }



    //разделить запросы из бд и сетевой в зависимости от картинки...............

    val ImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(PhotoApi.IMAGE_BASE_URL + detailsState.photo?.url)
            //.size(Size.ORIGINAL)
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
                        //или через if
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

                //кнопка скачивания и кнопка для закладки

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(200.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate("Home")
                            //download

                            //add path logic
//                            val downloader = AndroidDownloader(this)
//                            downloader.downloadFile("")
//

                        }
                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Download,
//                            contentDescription = ""
//                        )
//                        Text(
//                            modifier = Modifier.padding(start = 16.dp),
//                            text = stringResource(R.string.explore)
//                        )
                        Icon(
                            painter = painterResource(id = R.drawable.download_button),
                            contentDescription = "Download"
                        )
                    }
                    Button(
                        onClick = {
                            navController.navigate("Home")
                            //смена стейта, отрисовка другой иконки и добавление в избранные
                        }
                    ) {
//                        Icon(
//                            selected = R.drawable.bookmark_active,
//                            unselected = R.drawable.bookmark_inactive,
//                            contentDescription = ""
//                        )
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
                            .fillMaxSize(),
                        //.clip(RoundedCornerShape(12.dp))
                        //.background(MaterialTheme.colorScheme.primaryContainer),
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

