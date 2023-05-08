package com.raveline.diarymongoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.raveline.diarymongoapp.common.utlis.Constants
import com.raveline.diarymongoapp.common.utlis.retryUploadingImageToFirebase
import com.raveline.diarymongoapp.data.database.repository.ImagesUploadDao
import com.raveline.diarymongoapp.navigation.SetupNavGraph
import com.raveline.diarymongoapp.navigation.screens.Screens
import com.raveline.diarymongoapp.ui.theme.DiaryMongoAppTheme
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imagesUploadDao: ImagesUploadDao

    var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpened
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        FirebaseApp.initializeApp(this)

        setContent {
            DiaryMongoAppTheme(dynamicColor = true) {

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

        cleanupCheck(scope = lifecycleScope, imagesUploadDao = imagesUploadDao)
    }
}

private fun cleanupCheck(
    scope: CoroutineScope,
    imagesUploadDao: ImagesUploadDao
) {
    scope.launch(IO) {
        val result = imagesUploadDao.getImages()
        result.forEach { imageToUpload ->
            retryUploadingImageToFirebase(
                imageToUpload = imageToUpload,
                onSuccess = {
                    scope.launch(IO) {
                        imagesUploadDao.deleteImage(
                            imageId = imageToUpload.id
                        )
                    }
                }
            )
        }
    }
}

private fun getStartDestination(): String {
    val user = App.create(Constants.MONGO_API_KEY).currentUser
    return if (user != null && user.loggedIn) Screens.HomeSplash.route
    else Screens.Authentication.route
}

