package com.makogon.alina.pinterest_pexels.photoList.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomLinearProgressBar() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 70.dp)) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            trackColor = Color.LightGray,
            color = Color.Red //progress color
        )
    }
}