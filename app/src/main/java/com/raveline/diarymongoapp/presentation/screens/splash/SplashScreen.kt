package com.raveline.diarymongoapp.presentation.screens.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.raveline.diarymongoapp.R
import com.raveline.diarymongoapp.navigation.screens.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeSplashScreen(navController: NavHostController) {

    Scaffold(content = {
        SplashScreen(navController)
    })

}

@Composable
private fun SplashScreen(navController: NavHostController) {

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

    LaunchedEffect(Unit ){
        delay(2500)
        navController.navigate(Screens.Home.route)
    }
}