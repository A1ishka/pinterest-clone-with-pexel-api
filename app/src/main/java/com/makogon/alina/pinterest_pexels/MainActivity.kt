package com.makogon.alina.pinterest_pexels

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.makogon.alina.pinterest_pexels.core.presentation.HomeScreen
import com.makogon.alina.pinterest_pexels.core.presentation.MainScreen
import com.makogon.alina.pinterest_pexels.core.presentation.MainViewModel
import com.makogon.alina.pinterest_pexels.core.presentation.NavItems
import com.makogon.alina.pinterest_pexels.details.presentation.DetailsScreen
import com.makogon.alina.pinterest_pexels.photoList.util.downloader.AndroidDownloader
import com.makogon.alina.pinterest_pexels.ui.theme.Pinterest_pexelsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>() //анимация иконки

    override fun onCreate(savedInstanceState: Bundle?) {


//        val downloader = AndroidDownloader(this)
//        downloader.downloadFile("")


        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.5f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.5f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
        setContent {
            Pinterest_pexelsTheme {

                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    MainScreen()
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavItems.Home.route
                    ) {
                        composable(NavItems.Home.route) {
                            HomeScreen(navController)
                        }
                        composable(
                            NavItems.Details.route + "/{photoId}",
                            arguments = listOf(navArgument("photoId") { type = NavType.IntType })
                        ) {
                                backStackEntry ->  DetailsScreen(backStackEntry, navController)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Pinterest_pexelsTheme {
        Greeting("Android")
    }
}