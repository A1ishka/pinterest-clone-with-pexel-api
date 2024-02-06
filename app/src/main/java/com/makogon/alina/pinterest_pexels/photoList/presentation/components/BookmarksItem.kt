package com.makogon.alina.pinterest_pexels.photoList.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.makogon.alina.pinterest_pexels.R
import com.makogon.alina.pinterest_pexels.core.presentation.NavItems
import com.makogon.alina.pinterest_pexels.photoList.data.remote.PhotoApi
import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun BookmarksItem(photo: Photo?, navHostController: NavHostController, modifier: Modifier) {

    val ImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(/*PhotoApi.IMAGE_BASE_URL +*/photo?.url)
            .placeholder(R.drawable.placeholder)
            .setHeader("Authorization", PhotoApi.API_KEY)
            .build()
    ).state

    Scaffold(
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(7.dp))
                    .clickable {
                        navHostController.navigate(NavItems.Details.route + "/${photo?.id}")
                    },
            ) {
                if (ImageState is AsyncImagePainter.State.Success) {
                    Box {
                        Image(
                            painter = ImageState.painter,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .defaultMinSize(minWidth = 30.dp, minHeight = 70.dp)
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Author: ${photo?.photographer}",
                                color = Color.White
                            )
                        }
                    }
                }
                if (ImageState is AsyncImagePainter.State.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .height(250.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    )
}