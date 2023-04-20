package com.raveline.diarymongoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.raveline.diarymongoapp.common.utlis.Constants
import com.raveline.diarymongoapp.data.model.MongoDB
import com.raveline.diarymongoapp.navigation.SetupNavGraph
import com.raveline.diarymongoapp.navigation.screens.Screens
import com.raveline.diarymongoapp.ui.theme.DiaryMongoAppTheme
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {

    var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpened
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        FirebaseApp.initializeApp(this)

        setContent {
           DiaryMongoAppTheme(dynamicColor = false) {

               val navController = rememberNavController()

               SetupNavGraph(
                   startDestination = getStartDestination(),
                   navController = navController,
                   onDataLoaded = {
                       keepSplashOpened = false
                   }
               )
           }

        }

    }
}

private fun getStartDestination(): String {
    val user = App.create(Constants.MONGO_API_KEY).currentUser
    return if (user != null && user.loggedIn) Screens.Home.route
    else Screens.Authentication.route
}

