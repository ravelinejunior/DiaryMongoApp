package com.raveline.diarymongoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.raveline.diarymongoapp.navigation.SetupNavGraph
import com.raveline.diarymongoapp.navigation.screens.Screens

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            val navController = rememberNavController()

            SetupNavGraph(
                startDestination = Screens.Authentication.route,
                navController = navController
            )

            SplashScreen()
        }

    }
}

@Composable
fun SplashScreen() {

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.splashnotes))

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            restartOnPlay = true,
            reverseOnRepeat = true,
            enableMergePaths = true,
        )
    }
}